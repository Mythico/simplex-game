/*
 * Button.java
 *
 * Created on March 23, 2008, 10:57 PM
 */

package mdes.oxy;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author davedes
 */
public class Button extends Label {
    
    private String action;
    private String actionCommand;
    
    private RenderState hover = null;
    private RenderState pressed = null;
    private RenderState disabled = null;
    private RenderState selectedDisabled = null;
    private RenderState selectedHover = null;
    private RenderState selectedPressed = null;
    private RenderState selectedDefault = null;
    private RenderState focusOverlay = null;
    
    public static final int STATE_DEFAULT = 2;
    public static final int STATE_PRESSED = 4;
    public static final int STATE_HOVER = 8;
    
    private int state = STATE_DEFAULT;
    
    private boolean sticky = false;
    
    ButtonGroup group;
    boolean selectedFlag = false;
    
    public float textOffset = 0f;
    public static final float OFFSET_AUTO = Float.MIN_VALUE;
    
    private boolean activateDown = false;
    private boolean stayDownOutside = false;
        
    /** Creates a new instance of Button */
    public Button(Desktop desktop) {
        super(desktop);
        setFocusable(true);
    }
    
    public void setActionCommand(String command) {
        this.actionCommand = command;
    }
    
    public String getActionCommand() {
        return actionCommand;
    }
    
    public int getState() {
        return state;
    }
    
    public void setSelected(boolean selected) {
        this.selectedFlag = selected;
        if (group!=null) {
            group.setSelected(this, selected);
        }
    }
    
    public boolean isSelected() {
        return selectedFlag;
    }
    
    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }
    
    public boolean isSticky() {
        return sticky;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getAction() {
        return action;
    }
    
    public void doPress() {
        if (group!=null) {
            group.setSelected(this, true);
        } else if (isSticky()) {
            setSelected(!isSelected());
        }
        onAction();
        if (action!=null)
            execScript(action);
    }
    
    protected void onAction() {
    }
    
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {
        if (bean == "textOffset") {
            if ("auto".equals(value))
                this.textOffset = OFFSET_AUTO;
            else
                this.textOffset = toFloat(bean, value);
            return true;
        } else if (bean == "action") {
            if (value!=null&&value.length()==0)
                value = null;
            setAction(value);
            return true;
        } else if (bean == "actionCommand") {
            setActionCommand(value);
            return true;
        } else if (bean == "group") {
            OxyDoc doc = getDesktop().getDoc();
            Object obj = doc.getElement(value);
            ButtonGroup grp;
            if (obj!=null) {
                if (!(obj instanceof ButtonGroup))
                    throw new OxyException("a named element already exists under the name "+value);
                grp = (ButtonGroup)obj;
            } else {
                grp = new ButtonGroup();
                doc.putElement(value, grp);
            }
            grp.add(this);
            return true;
        } else {
            return super.handleSpecialSetter(bean, value);
        }
    }
    
    protected void renderComponent(Graphics g) {
        ComponentTheme ui = getTheme();
        float alpha = getAlpha();
        if (ui!=null && alpha!=0f) {
            RenderState r = getState(ui, state);
            ui.render(this, r);
            
            if (focusOverlay!=null && hasFocus())
                ui.render(this, focusOverlay);
        }
        float off = this.textOffset;
        if (off==OFFSET_AUTO)
            off = state==STATE_PRESSED ? 1.0f : 0.0f;
        renderImageAndText(g, off, off);
    }
    
    protected RenderState getState(ComponentTheme ui, int state) {
        RenderState r = ui.getDefaultState();
        if (isSelected()) {
            if (!isEnabled()) {
                r = selectedDisabled;
            } else if (state==STATE_DEFAULT) {
                r = selectedDefault;
            } else if (state==STATE_HOVER) {
                r = selectedHover;
            } else if (state==STATE_PRESSED)
                r = selectedPressed;
        } else {
            if (!isEnabled())
                r = disabled;
            else if (state==STATE_HOVER)
                r = hover;
            else if (state==STATE_PRESSED)
                r = pressed;
        }
        return r;
    }
    

    public void updateComponent(int delta) {
        int old = this.state;
        boolean mInside = isMouseInside();
        if (state==STATE_DEFAULT && mInside && getDesktop().getDragComponent()==null)
            state = STATE_HOVER;
        
        Input input = getDesktop().getContext().getInput();
        if (activateDown && (!hasFocus() || !input.isKeyDown(getDesktop().getControls().activate))) {
            state = mInside ? STATE_HOVER : STATE_DEFAULT;
            activateDown = false;
        }
    }
    
    protected void onButtonDown() {
    }
    
    protected void onButtonUp() {
    }
    
    protected void onKeyPress(int key, char c) {        
        if (isActionKey(key, c)) {
            int old = state;
            state = STATE_PRESSED;
            activateDown = true;
            if (old!=state)
                onButtonDown();
        }
    }

    protected void onKeyRelease(int key, char c) {
        if (activateDown && isActionKey(key, c)) {
            boolean mouseInside = isMouseInside();
            state = mouseInside ? STATE_HOVER : STATE_DEFAULT;
            onButtonUp();
            activateDown = false;
            doPress();
        }
    }
    
    protected boolean isActionKey(int key, char c) {
        return key == getDesktop().getControls().activate;
    }

    protected void onMouseRelease(int button, int x, int y, int mouseX, int mouseY) {
        if (button==0 && isEnabled()) {
            boolean mouseInside = isMouseInside();
            state = mouseInside ? STATE_HOVER : STATE_DEFAULT;
            onButtonUp();
            if (mouseInside) {
                doPress();
            }
        }
    }

    protected void onMousePress(int button, int x, int y, int mouseX, int mouseY) {
        if (button==0 && isEnabled()) {
            state = STATE_PRESSED;
            onButtonDown();
        }
    }

    protected void onMouseEnter(int x, int y, int mouseX, int mouseY) {
        Component drag = getDesktop().getDragComponent();
        if (drag==this && isEnabled())
            state = STATE_PRESSED;
        else if (state!=STATE_PRESSED)
            state = STATE_HOVER;
    }

    protected void onMouseExit(int x, int y, int mouseX, int mouseY) {
        if (state!=STATE_PRESSED || (!this.stayDownOutside && !activateDown)) {
            state = STATE_DEFAULT;
        }
    }

    protected void onThemeChange() {
        ComponentTheme ui = getTheme();
        if (ui!=null) {

            hover = ui.getState("hover");
            pressed = ui.getState("pressed");
            disabled = ui.getState("disabled");
            selectedDisabled = ui.getState("selectedDisabled");
            selectedHover = ui.getState("selectedHover");
            selectedPressed = ui.getState("selectedPressed");
            selectedDefault = ui.getState("selectedDefault");
            focusOverlay = ui.getState("focusOverlay");

            RenderState def = ui.getDefaultState();
            if (pressed==null)
                pressed = def;
            if (hover==null)
                hover = def;
            if (disabled==null)
                disabled = def;
            if (selectedDefault==null)
                selectedDefault = hover;
            if (selectedHover==null)
                selectedHover = selectedDefault;
            if (selectedDisabled==null)
                selectedDisabled = disabled;
            if (selectedPressed==null)
                selectedPressed = pressed;
        }
    }

    public boolean isStayDownOutside() {
        return stayDownOutside;
    }

    public void setStayDownOutside(boolean stayDownOutside) {
        this.stayDownOutside = stayDownOutside;
    }
}
