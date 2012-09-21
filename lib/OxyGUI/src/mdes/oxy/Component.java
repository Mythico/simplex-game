package mdes.oxy;

import java.lang.reflect.Method;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

/*
 * Component.java
 *
 * Created on March 22, 2008, 1:04 PM
 */

/**
 * The base widget class.
 * @author davedes
 */
public class Component {
    
    /**
     * The x anchor.
     */
    public final Anchor x = new Anchor(this, Anchor.X);
    /**
     * The y anchor.
     */
    public final Anchor y = new Anchor(this, Anchor.Y);
    /**
     * The width anchor.
     */
    public final Anchor width = new Anchor(this, Anchor.WIDTH);
    /**
     * The height anchor.
     */
    public final Anchor height = new Anchor(this, Anchor.HEIGHT);
    
    //values used for onResize/onMove handlers
    private float lastX = Float.MIN_VALUE;
    private float lastY = Float.MIN_VALUE;
    private float lastW = Float.MIN_VALUE;
    private float lastH = Float.MIN_VALUE;
    
    /**
     * The parent component.
     */
    Panel parent;
    /**
     * Whether the mouse is inside this component's bounds.
     */
    boolean mouseInside = false;
    /**
     * Whether this component has the focus.
     */
    boolean hasFocus = false;
        
    /**
     * The "default" z-index layer (0).
     */
    public static final int DEFAULT_LAYER = 0;
    /**
     * The window layer (100).
     */
    public static final int WINDOW_LAYER = 100;
    /**
     * The top-level window layer (200).
     */
    public static final int TOP_LAYER = 200;
    /**
     * The modal layer (300).
     */
    public static final int MODAL_LAYER = 300;
    /**
     * The popup layer (400).
     */
    public static final int POPUP_LAYER = 400;
    
    //component values... see getter/setter methods by the same name
    private boolean centeringX = false;
    private boolean centeringY = false; 
    private ComponentTheme ui;
    private int zIndex = 0;
    private boolean visible = true;
    private boolean opaque = false;
    private Color background = new Color(1f,1f,1f);
    private Color foreground = new Color(0f,0f,0f);  
    private Color disabledForeground = new Color(.5f, .5f, .5f);
    private boolean enabled = true;
    private Rectangle bounds = new Rectangle(0f,0f,0f,0f);
    private boolean requestFocusEnabled = true;
    private float minWidth, minHeight;
    private float maxWidth, maxHeight;
    private Font font;
    private String name;
    String upstr,downstr,leftstr,rightstr;
    private boolean focusable = false;
    private String toolTipText = null;
    private float top;
    private float left;
    private float right;
    private float bottom;
    /**
     * The next "up" traversal component.
     */
    public Component next_up;
    /**
     * The next "down" traversal component.
     */
    public Component next_down;
    /**
     * The next "left" traversal component.
     */
    public Component next_left;
    
    /**
     * The next "right" traversal component.
     */
    public Component next_right;
    /**
     * The alpha filter for this component.
     */
    protected Color alphaFilter = new Color(1f, 1f, 1f, 1f);
    
    private Desktop desktop;
    /**
     * The top left corner's width for this component's inside bounds.
     */
    public float xOff;
    public float yOff;
    public float xOff2;
    
    /**
     * The bottom right corner's height for this component's inside bounds.
     */
    public float yOff2;
        
    /**
     * Creates a new instance of this component in the specified desktop.
     */
    public Component(Desktop desktop) {
        this.desktop = desktop;
    }
    
    /**
     * Used internally.
     */
    Component() {
    }
    
    /**
     * Sets the offsets that define the "inside" bounds for this component. The resulting
     * bounds are used by the contains. The first two offsets are the width and height 
     * to offset from (0, 0). The second two offsets are the width and height to offset
     * from (width, height).
     * @param xOff the top left width
     * @param yOff the top left height
     * @param xOff2 the bottom right width
     * @param yOff2 the bottom right height
     */
    public void setInsideOffsets(float xOff, float yOff, float xOff2, float yOff2) {
        this.xOff = xOff;
        this.yOff = yOff;
        this.xOff2 = xOff2;
        this.yOff2 = yOff2;
    }
    
    /**
     * Sets the name for this component. If this component was created through XML, 
     * the name change will not be reflected in the OxyDoc object.
     * @param name changes the name for this component
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the name for this component, or null if it does not have one.
     * @return the name of this component
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns <tt>true</tt> if this component should consume key and mouse button
     * input after the events have been received, <tt>false</tt> otherwise.
     * @return <tt>true</tt> if this component should consume input
     */
    protected boolean isConsumingInput() {
        return true;
    }
    
    /**
     * Returns <tt>true</tt> if we can traverse with the specified
     * key, otherwise return <tt>false</tt> and no traversals will
     * be fired. By default, this method returns <tt>true</tt>.
     * @param key the key used to request the traversal
     * @param c the char used to request the traversal
     * @return <tt>true</tt> if traversals are available.
     */
    protected boolean isTraversalAvailable(int key, char c) {
        return true;
    }
    
    /**
     * Called to resolve any references after the entire document has been parsed.
     * @param doc the document used
     * @throws mdes.oxy.OxyException if there was an error in resolving
     */
    protected void resolveReferences(OxyDoc doc) throws OxyException {
        try {
            if (upstr!=null && upstr.length()!=0)
                next_up = (Component)doc.getElement(upstr);
            if (leftstr!=null && leftstr.length()!=0)
                next_left = (Component)doc.getElement(leftstr);
            if (downstr!=null && downstr.length()!=0)
                next_down = (Component)doc.getElement(downstr);
            if (rightstr!=null && rightstr.length()!=0)
                next_right = (Component)doc.getElement(rightstr);
        } catch (ClassCastException e) {
            throw new OxyException("error casting next_xx attribute to Component");
        }
    }
    
    /**
     * Returns the owner of this control, or <tt>null</tt> if this
     * component is not a control. A control is one such as a Spinner's
     * button or text field.
     * @return the owner of this control, or <tt>null</tt> if 
     * this component is not a control
     */
    protected Component getControlOwner() {
        return null;
    }
    
    /**
     * A convenience method to execute the given method.
     * @param call the scripted method and parameters
     * @return the return value of the method, or null if it does not have one
     */
    protected Object execScript(String call) {
        if (call==null||call.length()==0)
            return null;
        Desktop desktop = getDesktop();
        Object client = desktop.getClient();
        Doc doc = desktop.getDoc();
        
        int left = call.indexOf('(');
        String name = call.substring(0, left);
        String args = call.substring(left+1, call.length()-1);
        
        try {
            Object[] params;
            if (args.trim().length()==0)
                params = null;
            else
                params = Doc.parseParameters(doc, this, args);
            Method m = Doc.findMethod(client, name, params);
            return Doc.invokeMethod(m, client, params);
        } catch (OxyException e) {
            Desktop.warn("error invoking method "+name+" with args: "+args, e);
            return null;
        }
    }
        
    /**
     * Handles a unique setter (such as setBackground which requires a color).
     * Returns <tt>true</tt> if the given bean/value pair was changed successfully, 
     * or <tt>false</tt> if it still requires changing.
     * @param bean the bean (eg. "background")
     * @param value the value (eg. "#f0f0f0")
     * @throws mdes.oxy.OxyException if an error occurred while parsing the setter
     * @return <tt>true</tt> if the bean/value was set
     */
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {
        if (bean == "name") {
            setName(value);
            return true;
        } else if (bean == "focused") {
            if ("true".equals(value))
                grabFocus();
            else if ("false".equals(value))
                releaseFocus();
            else
                throw new OxyException("cannot parse focus with value "+value);
            return true;
        } else if (bean == "top") {
            setTop(toFloat(bean, value));
            return true;
        } else if (bean=="left") {
            setLeft(toFloat(bean, value));
            return true;
        } else if (bean=="bottom") {
            setBottom(toFloat(bean, value));
            return true;
        } else if (bean=="right") {
            setRight(toFloat(bean, value));
            return true;
        } else if (bean == "background") {
            setBackground(Doc.parseColor(value));
            return true;
        } else if (bean == "foreground") {
            setForeground(Doc.parseColor(value));
            return true;
        } else if (bean == "toolTipText") {
            setToolTipText(value);
            return true;
        } else if (bean == "theme") {
            return true;
        } else if (bean == "disabledForeground") {
            setDisabledForeground(Doc.parseColor(value));
            return true;
        } else if (bean == "next_up") {
            upstr = value;
            return true;
        } else if (bean == "next_left") {
            leftstr = value;
            return true;
        } else if (bean == "next_down") {
            downstr = value;
            return true;
        } else if (bean == "next_right") {
            rightstr = value;
            return true;
        } else if (bean == "class" || bean == "factory") {
            return true;
        } else
            return false;
    }
    
    /**
     * Convenience method to turn a token into a float.
     * @param bean the bean
     * @param value the value
     * @throws mdes.oxy.OxyException if an error occurred
     * @return the float result
     */
    protected float toFloat(String bean, String value) throws OxyException {
        try { 
            return Float.parseFloat(value); 
        } catch (NumberFormatException e) { 
            throw new OxyException("cannot parse '"+bean+"' with float value: "+value); 
        }
    }
    
    /**
     * Copies the specified color to the alphaFilter values.
     * @param copy the copy to use, or null if a new instance is to be created
     * @param orig the original
     * @return the copied color
     */
    protected Color getAlphaCopy(Color copy, Color orig) {
        if (copy==null)
            copy = new Color(orig);
        copy.r = orig.r;
        copy.g = orig.g;
        copy.b = orig.b;
        copy.a = alphaFilter!=null ? alphaFilter.a : orig.a;
        return copy;
    }
    
    /**
     * Returns <tt>true</tt> if this component can receive focus.
     * @return whether this component can receive focus
     */
    public boolean isFocusAvailable() {
        if (!isEnabled() || !isRequestFocusEnabled() || !isFocusable() || !isShowing())
            return false;
        else {
            Window modal = getDesktop().getModalWindow();
            if (modal!=null && modal!=this && modal.isVisible()) {
                return this.isDeepChildOf(modal);
            }
        }
        return true;
    }
    
    /**
     * The desktop root component.
     * @return the desktop
     */
    public Desktop getDesktop() {
        return desktop;
    }
    
    public void render(Graphics g) {
        if (isShowing()) {
            Rectangle bounds = getBoundsOnScreen();
            
            if (isOpaque() && background!=null) {
                g.setColor(background);
                g.fill(bounds);
            }
            renderComponent(g);
        }
    }
    
    public void update(int delta) {
        if (!isShowing() && this.hasFocus())
            getDesktop().setFocusOwner(null);
        
        //check move
        float oldX = x.get();
        float oldY = y.get();
        if (oldX!=lastX || oldY!=lastY)
            onMove();
        lastX = oldX;
        lastY = oldY;
        
        //check resize
        float oldW = width.get();
        float oldH = height.get();
        if (oldW!=lastW || oldH!=lastH)
            onResize();
        lastW = oldW;
        lastH = oldH;
        
        updateComponent(delta);
    }
    
    public float getXOnScreen() {
        return (parent==null) ? x.get() : x.get()+parent.getXOnScreen();
    }
    
    public float getYOnScreen() {
        return (parent==null) ? y.get() : y.get()+parent.getYOnScreen();
    }
    
    protected void updateComponent(int delta) {
    }
    
    public boolean contains(float x, float y) {
        if (ui!=null)
            return ui.contains(this, x, y);
        else
            return inside(x, y);
    }
    
    public boolean inside(float x, float y) {
        float ax=getXOnScreen(), ay=getYOnScreen();
        return x>=ax+xOff && y>=ay+yOff && x<=ax+width.get()-xOff2 && y<=ay+height.get()-yOff2;
    }
    
    protected void renderComponent(Graphics g) {
        if (ui!=null) {
            ui.render(this);
        }
    }
    
    public boolean isShowing() {
        boolean ret = false;
        if (width.get()<=0 || height.get()<=0) //empty bounds
            ret = false;
        else if (isVisible()) //visible, but needs a showing parent
            ret = (parent == null) || parent.isShowing();
        return ret;
    }
    
    public Rectangle getBoundsOnScreen() {
        bounds.setX(getXOnScreen());
        bounds.setY(getYOnScreen());
        bounds.setWidth(width.get());
        bounds.setHeight(height.get());
        return bounds;
    }
    
    void setWindowsActive(boolean b) {
        Desktop root = getDesktop();
        root.clearActiveWindows();
        Component top = this;
        while (top!=null) {
            if (top instanceof Window) {
                Window win = (Window)top;
                boolean old = win.isActive();
                win.setActive(b);
                if (b)
                    root.activeWindows.add(win);
            }
            top = top.parent;
        }
    }
    
    public void grabFocus() {
        getDesktop().setFocusOwner(this);
        setWindowsActive(true);
    }
    
    public boolean hasFocus() {
        if (hasFocus && !isEnabled()) {
            this.releaseFocus();
        }
        return hasFocus;
    }
    
    public void releaseFocus() {
        if (hasFocus()) {
            getDesktop().setFocusOwner(null);
            setWindowsActive(false);
        }
    }
        
    public void setPadding(float p) {
        setPadding(p, p);
    }

    public void setPadding(float h, float v) {
        setPadding(v, h, v, h);
    }
    
    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        if (this.top!=top) {
            this.top = top;
            onPaddingChange();
        }
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        if (this.left!=left) {
            this.left = left;
            onPaddingChange();
        }
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        if (this.right!=right) {
            this.right = right;
            onPaddingChange();
        }
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        if (this.bottom!=bottom) {
            this.bottom = bottom;
            onPaddingChange();
        }
    }

    public void setPadding(float top, float left, float bottom, float right) {
        this.setTop(top);
        this.setLeft(left);
        this.setBottom(bottom);
        this.setRight(right);
    }
    
    public void setAlpha(float alpha) {
        if (alpha<0f||alpha>1f)
            throw new IllegalArgumentException("alpha value must be: >= 0.0 and <= 1.0");
        if (alphaFilter!=null)
            alphaFilter.a = alpha;
    }
    
    public float getAlpha() {
        return alphaFilter!=null ? alphaFilter.a : 1f;
    }
    
    public void setX(Position p) {
        x.set(p.getValue(), p.isFixed());
    }
    
    public void setY(Position p) {
        y.set(p.getValue(), p.isFixed());
    }
    
    public void setWidth(Position p) {
        width.set(p.getValue(), p.isFixed());
    }
    
    public void setHeight(Position p) {
        height.set(p.getValue(), p.isFixed());
    }
    
    public float getWidth() {
        return width.get();
    }
    
    public float getHeight() {
        return height.get();
    }
    
    public float getX() {
        return x.get();
    }
    
    public float getY() {
        return y.get();
    }
    
    public void setLocation(Position x, Position y) {
        setX(x);
        setY(y);
    }
    
    public void setSize(Position width, Position height) {
        setWidth(width);
        setHeight(height);
    }
    
    public void setBounds(Position x, Position y, Position width, Position height) {
        setLocation(x, y);
        setSize(width, height);
    }
    
    public Panel getParent() {
        return parent;
    }

    public boolean isMouseInside() {
        return mouseInside;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
        if (parent!=null)
            parent.childrenDirty = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isOpaque() {
        return opaque;
    }

    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    //whether this component is enabled
    public boolean isEnabled() {
        return enabled;
    }

    //whether this component is enabled
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    //whether to request focus on grab
    public boolean isRequestFocusEnabled() {
        return requestFocusEnabled;
    }

    //whether to request focus on grab
    public void setRequestFocusEnabled(boolean requestFocusEnabled) {
        this.requestFocusEnabled = requestFocusEnabled;
    }
    
    //////////////////////////////////////
    //    MAX/MIN SIZES                 //
    //////////////////////////////////////
    
    public float getMinWidth() {
        return width.min;
    }

    public void setMinWidth(float minWidth) {
        width.setMin(minWidth);
    }

    public float getMinHeight() {
        return height.min;
    }

    public void setMinHeight(float minHeight) {
        height.setMin(minHeight);
    }

    public float getMaxWidth() {
        return width.max;
    }

    public void setMaxWidth(float maxWidth) {
        width.setMax(maxWidth);
    }

    public float getMaxHeight() {
        return height.max;
    }

    public void setMaxHeight(float maxHeight) {
        height.setMax(maxHeight);
    }
        
    //whether XY-centering is enabled
    public boolean isCenteringEnabled() {
        return isCenteringX() && isCenteringY();
    }

    //whether XY-centering is enabled
    public void setCenteringEnabled(boolean x, boolean y) {
        setCenteringX(x);
        setCenteringY(y);
    }
    
    //whether XY-centering is enabled
    public void setCenteringEnabled(boolean centeringEnabled) {
        setCenteringEnabled(centeringEnabled, centeringEnabled);
    }
    
    //whether X-centering is enabled
    public boolean isCenteringX() {
        return centeringX;
    }

    //whether X-centering is enabled
    public void setCenteringX(boolean centeringX) {
        this.centeringX = centeringX;
    }

    //whether Y-centering is enabled
    public boolean isCenteringY() {
        return centeringY;
    }

    //whether Y-centering is enabled
    public void setCenteringY(boolean centeringY) {
        this.centeringY = centeringY;
    }
    
    //gets the current font
    public Font getFont() {
        if (font==null)
            return getDesktop().getDefaultFont();
        else
            return font;
    }

    //changes the font
    public void setFont(Font font) {
        this.font = font;
    }
    
    //gets the current theme, or null
    public ComponentTheme getTheme() {
        return ui;
    }
    
    //installs a new theme
    public void setTheme(ComponentTheme ui) {
        ComponentTheme old = this.ui;
        this.ui = ui;
        if (old != this.ui) {
            if (this.ui!=null) {
                try {
                    this.ui.initComponent(this);
                } catch (OxyException e) {
                    Log.error("error initializing component", e);
                }             
            }
            onThemeChange();
        }
    }
    
    //disabled text color
    public Color getDisabledForeground() {
        return disabledForeground;
    }

    //disabled text color
    public void setDisabledForeground(Color disabledForeground) {
        this.disabledForeground = disabledForeground;
    }
    
    //focusable components can receive key and mouse wheel input
    public boolean isFocusable() {
        return focusable;
    }

    //focusable components can receive key and mouse wheel input
    public void setFocusable(boolean focusable) {
        if (focusable==false && hasFocus())
            releaseFocus();
        this.focusable = focusable;
    }
    
    
    //convenience to see if this component exists in the given ancestor
    public boolean isDeepChildOf(Panel ancestor) {
        Panel p = this.parent;
        while (p!=null) {
            if (p == ancestor)
                return true;
            p = p.parent;
        }
        return false;
    }
    
    //tool tips
    
    public String getToolTipText() {
        return toolTipText;
    }

    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }
    
    //////////////////////////////////////
    //    EVENT HANDLELRS               //
    //////////////////////////////////////
    
    protected void onMove() {
    }
    
    protected void onResize() {
    }
    
    protected void onThemeChange() {
    }
    
    protected void onMouseWheelMove(int amount) {
    }
    
    protected void onMouseDrag(int button, int x, int y, int mouseX, int mouseY) {
    }
    
    protected void onMousePress(int button, int x, int y, int mouseX, int mouseY) {
    }
    
    protected void onMouseRelease(int button, int x, int y, int mouseX, int mouseY) {
    }
    
    protected void onMouseMove(int x, int y, int mouseX, int mouseY) {
    }
    
    protected void onMouseEnter(int x, int y, int mouseX, int mouseY) {
    }
    
    protected void onMouseExit(int x, int y, int mouseX, int mouseY) {
    }
    
    protected void onKeyPress(int key, char c) {
    }
    
    protected void onKeyRelease(int key, char c) {
    }
    
    protected void onKeyRepeat(int key, char c) {
    }
    
    protected void onControllerPressed(int controller, int button) {
    }
    
    protected void onControllerReleased(int controller, int button) {
    }
    
    protected void onFocusLost() {
    }
    
    protected void onFocusGained() {
    }

    protected void onPaddingChange() {
    }
    
    public void onPostParse(OxyDoc doc) throws OxyException {
    }

}
