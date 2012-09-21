/*
 * Desktop.java
 *
 * Created on March 22, 2008, 1:47 PM
 */

package mdes.oxy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.InputAdapter;
import org.newdawn.slick.util.Log;

/**
 *
 * @author davedes
 */
public class Desktop extends Panel {
    
    static SkinDoc skin;
    private static SkinDoc defaultSkin = null;
    public static final String DEFAULT_SKIN_PATH = "res/skin/aqua.xml";

    private static WarningHandler warningHandler;
    
    public static interface WarningHandler {
        public void handleWarning(String msg, Throwable t);
    }
    
    public static WarningHandler getWarningHandler() {
        return warningHandler;
    }
    
    public static void setWarningHandler(WarningHandler aHandler) {
        warningHandler = aHandler;
    }
    
    public static void warn(String msg, Throwable t) {
        if (warningHandler!=null)
            warningHandler.handleWarning(msg, t);
    }

    public static SkinDoc getDefaultSkin() {
        if (defaultSkin==null) {
            try {
                defaultSkin = new SkinDoc(DEFAULT_SKIN_PATH);
            } catch (OxyException e) {
                Log.error("cannot load default skin", e);
            }
        }
        return defaultSkin;
    }
    
    public static void setSkin(SkinDoc aSkin) {
        if (aSkin==null)
            throw new IllegalArgumentException("skin is null");
        skin = aSkin;
    }
    
    public static SkinDoc getSkin() {
        if (skin==null)
            skin = getDefaultSkin();
        return skin;
    }
    
    private int lastDisplayWidth;
    private int lastDisplayHeight;
    private GUIContext context;
    private Input input;
    
    private int mouseX=-1, mouseY=-1, mouseButton=-1;
    private Component mouseOver;
    private SlickListener SLICK_LISTENER = new SlickListener();
    private boolean dragging = false;
    private Component dragComponent;
    private int dragButton = -1;
    private OxyDoc doc;
    private Component focusOwnerBeforeModal;
    private Component lastFocusOwner;
    private Component focusOwner;
    private Controls controls = new Controls();
    private Object client;
    Window modalWindow = null;
    List popups = new ArrayList();
    List activeWindows = new ArrayList();
    
    private String modalAction = null;
    private boolean scratch = false;
    private static final int MODAL_BLACK_IN = 1;
    private static final int MODAL_BLACK_OUT = -1;
    private static final int MODAL_PAUSE = 0;
    private Color modalScreen = new Color(0f, 0f, 0f, 0.0f);
    private float modalScreenMax = 0.65f;
    private int modalFade = MODAL_PAUSE;
    private boolean autoRenderModal = true;
    String modalStr;
    
    public static final int STANDARD_INITIAL_REPEAT_DELAY = 400;
    public static final int STANDARD_REPEAT_DELAY = 50;
    
    private static boolean defaultRepeatEnabled = true;
    private static int defaultRepeatDelay = STANDARD_REPEAT_DELAY;
    private static int defaultInitialRepeatDelay = STANDARD_INITIAL_REPEAT_DELAY;
    
    private Timer keyRepeats = new Timer(defaultRepeatDelay);
    private boolean keyRepeating = defaultRepeatEnabled;
    
    private ToolTip toolTip;
    Timer tipShowTimer = new Timer(750);
    Timer tipHideTimer = new Timer(4000);
        
    /** Creates a new instance of Desktop */
    public Desktop(Object client, GUIContext context, OxyDoc doc) {
        this.client = client;
        this.context = context;
        this.doc = doc;
        this.input = context.getInput();
        input.addPrimaryListener(SLICK_LISTENER);
                
        setSize(new Percent(1f), new Percent(1f));
        
        keyRepeats.setDelay(defaultRepeatDelay);
        keyRepeats.setInitialDelay(defaultInitialRepeatDelay);
        keyRepeats.setRepeats(true);
        
        toolTip = new ToolTip(this);
        toolTip.setVisible(false);
        toolTip.setTheme(SkinDoc.getDefaultTheme(doc, "toolTip"));
        tipShowTimer.setDelay(toolTip.getShowDelay());
        tipHideTimer.setDelay(toolTip.getHideDelay());
        tipShowTimer.setRepeats(false);
        tipHideTimer.setRepeats(false);
        add(toolTip);
        
        lastDisplayWidth = context.getWidth();
        lastDisplayHeight = context.getHeight();
    }
        
    public static Desktop parse(Object client, GUIContext context, String ref) throws OxyException {
        OxyDoc doc = new OxyDoc(client, context, ref);
        return doc.getDesktop();
    }
    
    
    public void setToolTip(ToolTip toolTip) {
        if (toolTip==null)
            throw new NullPointerException();
        remove(this.toolTip);
        this.toolTip = toolTip;
        
        toolTip.setVisible(false);
        tipShowTimer.setDelay(toolTip.getShowDelay());
        tipHideTimer.setDelay(toolTip.getHideDelay());
        add(this.toolTip);
    }
    
    public ToolTip getToolTip() {
        return toolTip;
    }
    
    protected void resolveReferences(OxyDoc doc) throws OxyException {
        try {
            if (modalStr!=null && modalStr.length()!=0) {
                Window w = (Window)doc.getElement(modalStr);
                if (w!=null) {
                    w.setVisible(false);
                }
                setModalWindow(w);
            }
        } catch (ClassCastException e) {
            throw new OxyException("error casting modalWindow "+modalStr+" to Window");
        }
    }
    
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {
        if (bean == "modalAction") {
            setModalAction(value);
            return true;
        } else if (bean == "modalWindow") {
            modalStr = value;
            return true;
        } else {
            return super.handleSpecialSetter(bean, value);
        }
    }
    
    public Font getDefaultFont() {
        Font f = getSkin().getDefaultFont();
        if (f == null)
            return getContext().getDefaultFont();
        else
            return f;
    }
    
    public OxyDoc getDoc() {
        return doc;
    }
    
    public Object getClient() {
        return client;
    }
    
    protected boolean isConsumingInput() {
        return false;
    }
    
    public GUIContext getContext() {
        return context;
    }
    
    public Desktop getDesktop() {
        return this;
    }
    
    public Image getSheet() {
        return getSkin().getDefaultSheet();
    }
    
    public int getMouseX() {
        return mouseX;
    }
    
    public int getMouseY() {
        return mouseY;
    }
    
    public Component getDragComponent() {
        return dragComponent;
    }
    
    void setFocusOwner(Component c) {
        Component old = focusOwner;
        focusOwner = c;
        if (c!=null) {
            c.hasFocus = true;
            c.onFocusGained();
        }
        if (old!=c) {
            lastFocusOwner = old;
            if (old!=null) {
                old.hasFocus = false;
                old.onFocusLost();
            }
        }
    }
    
    public void setModalWindow(Window window) {
        this.modalWindow = window;
        if (window==null) {
            modalScreen.a = 0f;
            modalFade = MODAL_PAUSE;
        }
    }
    
    public Window getModalWindow() {
        return modalWindow;
    }
    
    public Controls getControls() {
        return controls;
    }
    
    public void setModalAction(String action) {
        this.modalAction = action;
    }
    
    protected void onModalAction() {
    }
    
    void updateTipDelays() {
        tipShowTimer.setDelay(toolTip.getShowDelay());
        tipHideTimer.setDelay(toolTip.getHideDelay());
        System.out.println("updating tips: show "+tipShowTimer.getDelay());
    }
    
    void modalVisibilityChanged(boolean visible, boolean old) {
        if (modalWindow==null)
            return;
        if (old!=visible) {
            if (visible) {
                focusOwnerBeforeModal = getFocusOwner();
                setFocusOwner(null);
                toolTip.setVisible(false);
                tipShowTimer.stop();
                tipHideTimer.stop();
                if (modalWindow.getZIndex() < Component.MODAL_LAYER)
                    modalWindow.setZIndex(Component.MODAL_LAYER);
                modalWindow.grabFocus();
            } else {
                onModalAction();
                if (modalAction!=null)
                    execScript(modalAction);
                setFocusOwner(focusOwnerBeforeModal);
            }
            modalFade = visible ? MODAL_BLACK_IN : MODAL_BLACK_OUT;
            scratch = true;
        }
    }

    public void setControls(Controls controls) {
        if (controls==null)
            throw new NullPointerException();
        this.controls = controls;
    }
    
    public Component getFocusOwner() {
        return focusOwner;
    }
    
    public Component getDeepestComponentAt(Panel parent, int x, int y) {
        if (!parent.contains(x, y)) {
            return null;
        }
        
        for (int i=parent.getChildCount()-1; i>=0; i--) {
            Component comp = parent.getChild(i);
            
            if (comp!=null && comp.isShowing()) {
                if (comp.contains(x, y)) {                    
                    if (comp instanceof ToolTip) {
                        Component owner = ((ToolTip)comp).getOwner();
                        if (owner.contains(x, y))
                            return owner;
                    }
                    if (comp instanceof Panel && 
                            ((Panel)comp).getChildCount()>0)
                        return getDeepestComponentAt((Panel)comp, x, y);
                    else {
                        return comp;
                    }
                }
            }
        }
        return parent;
    }
    
    void clearActiveWindows() {
        for (int i=0; i<activeWindows.size(); i++) {
            ((Window)activeWindows.get(i)).setActive(false);
        }
        activeWindows.clear();
    }
    
    /**
     * Called to recursively render all children of this container.
     *
     * @param container the GUIContext we are rendering to
     * @param g the Graphics context we are rendering with
     */
    protected void renderChildren(Graphics g) {
        for (int i=0; i<getChildCount(); i++) {
            Component child = getChild(i);
            if (child.getZIndex()>=Component.MODAL_LAYER)
                return;
            child.render(g);
        }
    }
    
    public void render(Graphics g) {
        super.render(g);
        if (autoRenderModal) {
            renderModalLayer(g);
        }
    }
    
    protected void renderComponent(Graphics g) {
        super.renderComponent(g);
        ComponentTheme theme = getTheme();
        if (theme!=null) {
            theme.render(this);
        }
    }
    
    public void renderModalLayer(Graphics g) {
        if (modalWindow!=null) {
            if (modalScreen.a>0f) {
                g.setColor(modalScreen);
                g.fillRect(getYOnScreen(), getXOnScreen(), width.get(), height.get());
            }
        }
        for (int i=0; i<getChildCount(); i++) {
            Component child = getChild(i);
            if (child.getZIndex()>=Component.MODAL_LAYER) {
                child.render(g);
            }
        }
    }
    
    public boolean isAutoRenderModal() {
        return autoRenderModal;
    }

    public void setAutoRenderModal(boolean autoRenderModal) {
        this.autoRenderModal = autoRenderModal;
    }

    public float getModalScreenMax() {
        return modalScreenMax;
    }

    public void setModalScreenMax(float modalScreenMax) {
        if (modalScreenMax<0f || modalScreenMax>1f)
            throw new IllegalArgumentException("modalScreenMax must be >= 0.0 and <= 1.0");
        this.modalScreenMax = modalScreenMax;
    }
        
    private boolean outsideModalBounds(float x, float y) {
        if (modalWindow!=null && modalWindow.isShowing()) {
            if (!modalWindow.contains(mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Provides a hint for the creation of new text components to
     * enable key repeat with the specified delays. This does not
     * affect any existing instances of TextComponent. If individual
     * text component timing is desired, use the instance methods.
     * 
     * @param initialDelay the default initial delay for new text components
     *      to use
     * @param delay the default delay for new text components to use
     */
    public static void enableDefaultKeyRepeat(int initialDelay, int delay) {
        defaultRepeatEnabled = true;
        defaultInitialRepeatDelay = initialDelay;
        defaultRepeatDelay = delay;
    }
    
    /**
     * Provides a hint for the creation of new text components to
     * enable key repeat with the last used delays. This does not
     * affect any existing instances of TextComponent. If individual
     * text component timing is desired, use the instance methods. If 
     * no delays were last specified in enableDefaultKeyRepeat, the delays 
     * will be STANDARD_INITIAL_REPEAT_DELAY and STANDARD_REPEAT_DELAY.
     */
    public static void enableDefaultKeyRepeat() {
        defaultRepeatEnabled = true;
    }
    
    /**
     * Provides a hint for the creation of new text components to
     * disable key repeat. This does not affect any existing instances 
     * of TextComponent. If individual text component timing is desired, 
     * use the instance methods.
     */
    public static void disableDefaultKeyRepeat() {
        defaultRepeatEnabled = false;
    }
    
    /**
     * Returns <tt>true</tt> if new text component instances use key repeating.
     * 
     * @return <tt>true</tt> if new instances have key repeat enabled, <tt>false</tt> 
     *      otherwise
     */
    public static boolean isDefaultKeyRepeatEnabled() {
        return defaultRepeatEnabled;
    }
    
    /**
     * Enables key repeating on this text component instance with the specified delays.
     * If a "global" or default setting is desired, use the static methods instead. 
     * <p>
     * If the key repeats timer is currently running, it will be restarted to reflect the
     * new delays.
     *
     * @param initialDelay the initial delay before repeating keys
     * @param delay the delay between each key repeat
     */
    public void enableKeyRepeat(int initialDelay, int delay) {
        keyRepeating = true;
        keyRepeats.setDelay(initialDelay);
        keyRepeats.setInitialDelay(delay);
        if (keyRepeats.isRunning())
            keyRepeats.restart();
    }
    
    /**
     * Enables key repeating on this text component instance with the last used delays.
     * If a "global" or default setting is desired, use the static methods instead. If 
     * no delays were last specified in enableKeyRepeat, the delays will be 
     * STANDARD_INITIAL_REPEAT_DELAY and STANDARD_REPEAT_DELAY.
     * <p>
     * If the key repeats timer is already running, it will <i>not</i> be restarted.
     */
    public void enableKeyRepeat() {
        keyRepeating = true;
    }
    
    /**
     * Disables key repeating on this text component instance. 
     * If a "global" or default setting is desired, use the static methods instead.
     * <p>
     * If the key repeats timer is currently running, it will be stopped.
     */
    public void disableKeyRepeat() {
        keyRepeating = false;
        keyRepeats.stop();
    }
    
    /**
     * Returns <tt>true</tt> if this text component instance has key repeating enabled.
     * @return <tt>true</tt> if key repeating is enabled on this component, <tt>false</tt>
     *      otherwise
     */
    public boolean isKeyRepeatEnabled() {
        return keyRepeating;
    }
    
    public static String getClipboard() {
        return org.lwjgl.Sys.getClipboard();
    }
    
    public Input getInput() {
        return getContext().getInput();
    }
    
    protected void displayChanged() {
        input.removeListener(SLICK_LISTENER);
        input.addPrimaryListener(SLICK_LISTENER);
    }
    
    protected void updateComponent(int delta) {
        int cw = context.getWidth();
        int ch = context.getHeight();
        if (lastDisplayWidth != cw || lastDisplayHeight != ch) {
            displayChanged();
            lastDisplayWidth = cw;
            lastDisplayHeight = ch;
        }
        
        if (keyRepeating) {
            keyRepeats.update(context, delta);
            if (keyRepeats.isAction()) {
                SLICK_LISTENER.keyRepeated();
            }
        }
        
        float scale = 0.002f;
        if (modalFade!=MODAL_PAUSE) {
            modalScreen.a += modalFade * scale * delta;
            if (modalScreen.a>=modalScreenMax) {
                modalScreen.a = modalScreenMax;
                modalFade = MODAL_PAUSE;
            } else if (modalScreen.a<=0f) {
                modalScreen.a = 0f;
                modalFade = MODAL_PAUSE;
            }
            
            if (modalScreenMax==0f)
                modalFade = MODAL_PAUSE;
        }
        
        //super.updateComponent(delta);
        int lastX = mouseX;
        int lastY = mouseY;
        mouseX = input.getMouseX();
        mouseY = input.getMouseY();
        
        boolean modalBlock = outsideModalBounds(mouseX, mouseY);
        
        if (modalBlock && dragComponent==null) {
            if (mouseOver!=null && mouseOver.mouseInside) {
                int x1 = (int)(mouseX-mouseOver.getXOnScreen());
                int y1 = (int)(mouseY-mouseOver.getYOnScreen());
                mouseOver.onMouseExit(x1, y1, mouseX, mouseY);
                mouseOver.mouseInside = false;
            }
            return;
        }
        
        Component lastOver = mouseOver;
        mouseOver = getDeepestComponentAt(this, mouseX, mouseY);
        
        if (scratch || lastX!=mouseX || lastY!=mouseY || lastOver!=mouseOver) { //mouse has moved
            scratch = false;
            
            int mx1=0, my1=0;
            
            //if we have changed, send enter/exit events
            if (lastOver!=mouseOver && !modalBlock) {
                if (lastOver!=null && lastOver.mouseInside) {
                    int x1 = (int)(lastX-lastOver.getXOnScreen());
                    int y1 = (int)(lastY-lastOver.getYOnScreen());
                    lastOver.onMouseExit(x1, y1, lastX, lastY);
                    lastOver.mouseInside = false;
                }
                if (mouseOver!=null && !mouseOver.mouseInside) {
                    mx1 = (int)(mouseX-mouseOver.getXOnScreen());
                    my1 = (int)(mouseY-mouseOver.getYOnScreen()); 
                    mouseOver.onMouseEnter(mx1, my1, mouseX, mouseY);
                    mouseOver.mouseInside = true;
                }
            }
            
            //now motion events
            if (dragComponent!=null) { //dragging
                int x1 = (int)(mouseX-dragComponent.getXOnScreen());
                int y1 = (int)(mouseY-dragComponent.getYOnScreen());
                dragComponent.onMouseDrag(dragButton, x1, y1, mouseX, mouseY);
            } else if (!modalBlock && mouseOver!=null) { //moving
                if (mx1==0&&my1==0) {
                    mx1 = (int)(mouseX-mouseOver.getXOnScreen());
                    my1 = (int)(mouseY-mouseOver.getYOnScreen()); 
                }
                mouseOver.onMouseMove(mx1, my1, mouseX, mouseY);
            }
        }
        
        if (dragComponent!=null) {
            toolTip.setVisible(false);
            tipShowTimer.stop();
        } else if (lastX!=mouseX || lastY!=mouseY && lastOver==mouseOver) { //mouse moved
            if (!toolTip.isVisible()) { //restart if not showing
                tipShowTimer.restart();
                tipHideTimer.stop();
            }
        }
        
        if (lastOver!=mouseOver && mouseOver!=null) { //mouse over component changed
            toolTip.setVisible(false);
            tipShowTimer.restart();
            tipHideTimer.stop();
        } else if (mouseOver==null) {
            toolTip.setVisible(false);
            tipShowTimer.stop();
            tipHideTimer.stop();
        }
                        
        tipShowTimer.update(getContext(), delta);
        tipHideTimer.update(getContext(), delta);
        
        if (tipShowTimer.isAction()) {
            if (!toolTip.isVisible())
                showToolTip(mouseOver, mouseX, mouseY);
            tipHideTimer.restart();
        }
        if (tipHideTimer.isAction()) {
            if (toolTip.isVisible())
                toolTip.setVisible(false);
            tipShowTimer.stop();
        }
        
        if (toolTip!=null && toolTip.getOwner()!=null) {
            if (toolTip.isShowing() && !toolTip.getOwner().isShowing())
                toolTip.setVisible(false);
        }
    }
            
    private void showToolTip(Component c, int mouseX, int mouseY) {
        if (c == null)
            return;
        ToolTip toolTip = getToolTip();
        String str = c.getToolTipText();
        if (str!=null&&str.length()!=0) {
            float xoff = toolTip.getXOffset();
            float yoff = toolTip.getYOffset();
            toolTip.setOwner(c);
            toolTip.setText(str);
            toolTip.x.set(mouseX+xoff, true);
            toolTip.y.set(mouseY+yoff, true);
            toolTip.setVisible(true);
        }
    }
    
    public static class Controls {
        public int up = Input.KEY_UP;
        public int down = Input.KEY_DOWN;
        public int left = Input.KEY_LEFT;
        public int right = Input.KEY_RIGHT;
        public int cancel = Input.KEY_ESCAPE;
        
        //first tries next_right, then tries next_down
        public int nextFocus = Input.KEY_TAB;
        public int activate = Input.KEY_SPACE;
    }
    
    private class SlickListener extends InputAdapter {
        
        private int lastKey;
        private char lastChar;
    
        public void setInput(Input aInput) {
            input = aInput;
        }
        
        public boolean isAcceptingInput() {
            return isShowing();
        }
        
        protected void keyRepeated() {
            keyPressed(lastKey, lastChar, true);
        }
        
        public void keyPressed(int key, char ch) {
            keyPressed(key, ch, false);
        }
        
        private void keyPressed(int key, char ch, boolean isRepeat) {
            lastKey = key;
            lastChar = ch;
            if (!isRepeat && keyRepeating) {
                keyRepeats.restart();
            }
            
            if (!isRepeat && modalWindow!=null && modalWindow.isVisible() && modalWindow.isConsumingInput())
                input.consumeEvent();
            
            boolean findFocus;
            Component owner = getFocusOwner();
            if (owner!=null) {
                findFocus = owner instanceof Window;
                
                if (isRepeat)
                    owner.onKeyRepeat(key, ch);
                else
                    owner.onKeyPress(key, ch);
                
                if (!isRepeat && owner.isConsumingInput())
                    input.consumeEvent();
                
                if (owner.isTraversalAvailable(key, ch)) {
                    if (owner.getControlOwner()!=null)
                        owner = owner.getControlOwner();
                    Controls c = getControls();
                    Component next = null;
                    if (key == c.up) next = owner.next_up;
                    else if (key == c.left) next = owner.next_left;
                    else if (key == c.down) next = owner.next_down;
                    else if (key == c.right) next = owner.next_right;
                    else if (key == c.nextFocus)  {
                        if (input.isKeyDown(Input.KEY_LSHIFT)||input.isKeyDown(Input.KEY_RSHIFT)) {
                            next = owner.next_left;
                            if (next==null || !next.isFocusAvailable())
                                next = owner.next_up;
                        } else {
                            next = owner.next_right;
                            if (next==null || !next.isFocusAvailable())
                                next = owner.next_down;
                        }
                    }
                    if (next!=null && next.isFocusAvailable()) {
                        next.grabFocus();
                    }
                } else
                    findFocus = false;
            } else { //find focus?
                findFocus = true;
            }
            
            if (findFocus) {
                Controls c = getControls();
                if (key==c.up || key==c.left || key==c.down || key==c.right || key==c.nextFocus) {
                    Component f = lastFocusOwner;
                    if (f!=null && f.isFocusAvailable()) {
                        f.grabFocus();
                    } else {
                        f = findFirstFocusable(Desktop.this);
                        if (f!=null) {
                            f.grabFocus();
                        }
                    }
                }
            }
        }
        
        private Component findFirstFocusable(Component tree) {
            if (tree.isFocusAvailable() && !(tree instanceof Window))
                return tree;
            if (tree instanceof Panel) {
                Panel p = (Panel)tree;
                for (int i=0; i<p.getChildCount(); i++) {
                    Component child = p.getChild(i);
                    Component f = findFirstFocusable(child);
                    if (f!=null) {
                        return f;
                    }
                }
            }
            return null;
        }
        
        public void keyReleased(int key, char c) {
            if (lastKey==key) {
                keyRepeats.stop();
            }
            
            Component owner = getFocusOwner();
            if (owner!=null) {
                owner.onKeyRelease(key, c);
                if (owner.isConsumingInput())
                    input.consumeEvent();
            }
        }
        
        public void mousePressed(int button, int x, int y) {
            if (outsideModalBounds(mouseX, mouseY)) {
                input.consumeEvent();
                return;
            }
            
            if (mouseOver!=null) { //regular mouse press
                int x1 = (int)(mouseX-mouseOver.getXOnScreen());
                int y1 = (int)(mouseY-mouseOver.getYOnScreen());
                mouseOver.onMousePress(button, x1, y1, mouseX, mouseY);
                if (button==0 && mouseOver.isRequestFocusEnabled()) {
                    if (mouseOver.isEnabled() && mouseOver.isFocusable())
                        mouseOver.grabFocus();
                    else {
                        setFocusOwner(null);
                        mouseOver.setWindowsActive(true);
                    }
                } else {
                    mouseOver.setWindowsActive(true);
                }
                if (mouseOver.isConsumingInput())
                    input.consumeEvent();
                
                if (dragComponent==null) {
                    dragComponent = mouseOver;
                    dragButton = button;
                }
            } else { //clear focus
                Desktop.this.setFocusOwner(null);
                clearActiveWindows();
            }
        }
        
        public void mouseWheelMoved(int amount) {
            Component c = getFocusOwner();
            if (c!=null) {
                c.onMouseWheelMove(amount);
            }
        }
        
        public void mouseReleased(int button, int x, int y) {
            if (dragComponent!=null && dragButton==button) { //drag release
                int x1 = (int)(mouseX-dragComponent.getXOnScreen());
                int y1 = (int)(mouseY-dragComponent.getYOnScreen());
                dragComponent.onMouseRelease(button, x1, y1, mouseX, mouseY);
                if (dragComponent.isConsumingInput())
                    input.consumeEvent();
                dragComponent = null;
                dragButton = -1;
            } else {
                if (outsideModalBounds(mouseX, mouseY)) {
                    input.consumeEvent();
                    return;
                }
                
                if (mouseOver!=null) { //regular release
                    int x1 = (int)(mouseX-mouseOver.getXOnScreen());
                    int y1 = (int)(mouseY-mouseOver.getYOnScreen());
                    mouseOver.onMouseRelease(button, x1, y1, mouseX, mouseY);
                    if (mouseOver.isConsumingInput())
                        input.consumeEvent();
                }
            }
        }
    }

    
}
