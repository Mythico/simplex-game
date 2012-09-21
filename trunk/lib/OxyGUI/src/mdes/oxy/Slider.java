/*
 * Slider.java
 *
 * Created on March 27, 2008, 5:25 PM
 */

package mdes.oxy;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author davedes
 */
public class Slider extends Panel {
    
    public static final int HORIZONTAL = -1;
    public static final int VERTICAL = 1;
    
    private int orientation;
    private float value;
    private Timer slideDelay;
    private boolean trackDown = false;
    
    private Button thumbButton;
    private RenderState active;
    private RenderState inactive;
    private RenderState progress;
    
    private boolean locDirty = true;
    
    private float jumpSpace = 0.4f;
    
    private String changeAction = null;
    
    /**
     * Creates a new instance of Slider
     */
    public Slider(Desktop desktop, int orientation) {
        super(desktop);
        checkOrientation(orientation);
        this.orientation = orientation;
        
        setFocusable(true);
        
        slideDelay = new Timer(80);
        slideDelay.setInitialDelay(300);
        
        thumbButton = new Button(desktop) {
            
            private int lastX, lastY;
            
            protected void onMouseDrag(int button, int x1, int y1, int mouseX, int mouseY) {
                super.onMouseDrag(button, x1, y1, mouseX, mouseY);
                if (button!=0)
                    return;
                
                if (getOrientation()==HORIZONTAL) {
                    float start = Slider.this.getLeft();
                    float end = Slider.this.getWidth()-getWidth()-Slider.this.getRight();
                    
                    float old = this.x.get();
                    
                    float hw = getWidth()/2f;
                    float abx = Slider.this.getXOnScreen();
                    if (mouseX-hw < abx+start)
                        this.x.set(start, true);
                    else if (mouseX-hw > abx+end)
                        this.x.set(end, true);
                    else {
                        float nx = this.x.get()+mouseX-lastX;
                        nx = Math.min(Math.max(start, nx), end);
                        this.x.set(nx, true);
                    }
                    
                    if (old != this.x.get()) {
                        updateValue();
                        notifyChange();
                    }
                } else {
                    float start = Slider.this.getTop();
                    float end = Slider.this.getHeight()-getHeight()-Slider.this.getBottom();
                    
                    float old = this.y.get();
                    
                    float hh = getHeight()/2f;
                    float aby = Slider.this.getYOnScreen();
                    if (mouseY-hh < aby+start)
                        this.y.set(start, true);
                    else if (mouseY-hh > aby+end)
                        this.y.set(end, true);
                    else {
                        float ny = this.y.get()+mouseY-lastY;
                        ny = Math.min(Math.max(start, ny), end);
                        this.y.set(ny, true);
                    }
                    
                    if (old != this.y.get()) {
                        updateValue();
                        notifyChange();
                    }
                }
                                
                lastX = mouseX;
                lastY = mouseY;
            }

            protected void onMousePress(int button, int x1, int y1, int mouseX, int mouseY) {
                super.onMousePress(button, x1, y1, mouseX, mouseY);
                lastX = mouseX;
                lastY = mouseY;
            }
        };
        
        add(thumbButton);
    }
    
    private void checkOrientation(int orientation) {
        if (orientation!=HORIZONTAL && orientation!=VERTICAL)
            throw new IllegalArgumentException("slider orientation " +
                    "must be either HORIZONTAL or VERTICAL");
    }
            
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {
        if (bean == "changeAction") {
            setChangeAction(value);
            return true;
        } else
            return super.handleSpecialSetter(bean, value);
    }
    
    public void setValue(float value) {
        float old = this.value;
        if(value > 1) 
            value = 1;
        if(value < 0)
            value = 0;
        this.value = value;
        if (old!=this.value) {
            locDirty = true;
            notifyChange();
        }
    }
    
    public float getValue() {
        return value;
    }
    
    protected void onResize() {
        super.onResize();
        locDirty = true;
    }
    
    private void updateValue() {
        if (orientation==HORIZONTAL) {
            float x = thumbButton.getX()-getLeft();
            float w = getWidth()-thumbButton.getWidth()-getLeft()-getRight();
            this.value = w!=0f ? x/w : 0f;
        } else {
            float y = thumbButton.getY()-getTop();
            float h = getHeight()-thumbButton.getHeight()-getTop()-getBottom();
            this.value = h!=0f ? y/h : 0f;
        }
    }
    
    protected void notifyChange() {
        onChange();
        if (changeAction!=null)
            execScript(changeAction);
    }
    
    public void setChangeAction(String changeAction) {
        this.changeAction = changeAction;
    }
    
    public String getChangeAction() {
        return changeAction;
    }
    
    protected void onChange() {
    }
    
    public Button getThumbButton() {
        return thumbButton;
    }
        
    public int getOrientation() {
        return orientation;
    }
        
    protected void updateComponent(int delta) {
        super.updateComponent(delta);
                
        if (locDirty) {
            if (orientation==HORIZONTAL) {
                float x = getLeft() + (value * (getWidth()-thumbButton.getWidth()-getLeft()-getRight()));
                thumbButton.x.set(x, true);
            } else {
                float y = getTop() + (value * (getHeight()-thumbButton.getHeight()-getTop()-getBottom()));
                thumbButton.y.set(y, true);
            }
            locDirty = false;
        }
        
        Desktop d = getDesktop();
        Input input = d.getInput();
        
        if (trackDown && input.isMouseButtonDown(0) && isEnabled()) {
            int mouseX = d.getMouseX();
            int mouseY = d.getMouseY();
            
            slideDelay.update(d.getContext(), delta);
            if (slideDelay.isAction()) {
                jump(mouseX, mouseY);
            }
        } else if (trackDown) {
            trackDown = false;
            slideDelay.stop();
            lastMoveDir=0;
        }
    }
    
    private int lastMoveDir = 0;
    
    private void jump(int mouseX, int mouseY) {
        int moveDir = 0;
        if (orientation==HORIZONTAL) {
            float tx = thumbButton.getXOnScreen();
            
            if (mouseX > tx+thumbButton.getWidth())
                moveDir = 1;
            else if (mouseX < tx)
                moveDir = -1;         
        } else {
            float ty = thumbButton.getYOnScreen();
            
            if (mouseY > ty+thumbButton.getHeight())
                moveDir = 1;
            else if (mouseY < ty)
                moveDir = -1;     
        }
        
        if (lastMoveDir!=0 && lastMoveDir!=moveDir) {
            trackDown = false;
            slideDelay.stop();
            lastMoveDir = 0;
            return;
        }
        
        setValue(getValue() + (jumpSpace*moveDir));
        lastMoveDir = moveDir;
    }
    
    public void onMouseRelease(int button, int x, int y, int mouseX, int mouseY) {
        super.onMouseRelease(button, x, y, mouseX, mouseY);
        if (!isEnabled()||!thumbButton.isVisible()) {
            trackDown = false;
            return;
        }
        if (button==0) {
            trackDown = false;
            slideDelay.stop();
            lastMoveDir = 0;
        }
    }
    
    public void onMousePress(int button, int x, int y, int mouseX, int mouseY) {
        super.onMousePress(button, x, y, mouseX, mouseY);
        if (!isEnabled()||!thumbButton.isVisible()) {
            trackDown = false;
            return;
        }
        if (button==0) {
            trackDown = true;
            slideDelay.restart();
            jump(mouseX, mouseY);
        }
    }
    
    public void setThumbButtonVisible(boolean visible) {
        getThumbButton().setVisible(visible);
    }
    
    public boolean isThumbButtonVisible() {
        return getThumbButton().isVisible();
    }
    
    protected void renderComponent(Graphics g) {
        super.renderComponent(g);
        
        ComponentTheme ui = getTheme();
        if (ui!=null) {
            RenderState r = active;
            if (!hasFocus())
                r = inactive;
            ui.render(this, r);
            
            if (progress!=null) {
                float x = (int)(getLeft()+getXOnScreen());
                float y = (int)(getTop()+getYOnScreen());
                float w;
                float h;
                if (orientation==HORIZONTAL) {
                    w = (int)(value * (getWidth()-getLeft()-getRight()));
                    h = (int)(getHeight()-getBottom()-getTop());
                } else {
                    w = (int)(getWidth()-getRight()-getLeft());
                    h = (int)(value * (getHeight()-getTop()-getBottom()));
                }
                if (w!=0 && h!=0)
                    ui.render(this, progress, x, y, w, h, alphaFilter);
            }
        }
    }
    
    protected void onThemeChange() {
        ComponentTheme ui = getTheme();
        if (ui!=null) {
            active = ui.getState("active");
            inactive = ui.getState("inactive");
            progress = ui.getState("progress");
            if (inactive==null)
                inactive = active;
            
            OxyDoc doc = getDesktop().getDoc();
            ComponentTheme btnTheme = ui.getChild("thumbButton");
            if (btnTheme==null)
                btnTheme = SkinDoc.getDefaultTheme(doc, "button");
            getThumbButton().setTheme(btnTheme);
            thumbButton.setCenteringEnabled(false);
        }
    }

    public float getJumpSpace() {
        return jumpSpace;
    }

    public void setJumpSpace(float jumpSpace) {
        this.jumpSpace = jumpSpace;
    }
    
}