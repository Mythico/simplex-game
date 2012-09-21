/*
 * ScrollBar.java
 *
 * Created on March 29, 2008, 7:28 PM
 */

package mdes.oxy;

import org.newdawn.slick.Graphics;

/**
 *
 * @author davedes
 */
public class ScrollBar extends Panel {
    
    public static final int HORIZONTAL = -1;
    public static final int VERTICAL = 1;
    public static final int INCREMENT = 1;
    public static final int DECREMENT = -1;
    
    private Button incButton;
    private Button decButton;
    private Slider trackSlider;
        
    private boolean mouseWheelEnabled = true;
    
    private float scrollSpace = 0.2f;
    
    private Timer repeatTimer = new Timer(200);
    private int repeat = 0;
    
    /** Creates a new instance of ScrollBar */
    public ScrollBar(Desktop desktop, int orientation) {
        super(desktop);
        
        repeatTimer.setRepeats(true);
        
        trackSlider = new Slider(desktop, orientation) {
            protected void onChange() {
                onTrackChange();
            }
        };
        
        incButton = new Button(desktop) {
            protected void onButtonDown() {
                repeatTimer.restart();
                repeat = INCREMENT;
                onIncrement();
            }
            
            protected void onButtonUp() {
                repeatTimer.stop();
                repeat = 0;
            }
        };
        
        decButton = new Button(desktop) {
            protected void onButtonDown() {
                repeat = DECREMENT;
                repeatTimer.restart();
                onDecrement();
            }
            
            protected void onButtonUp() {
                repeatTimer.stop();
                repeat = 0;
            }
        };
        
        add(trackSlider);
        add(incButton);
        add(decButton);
    }
    
    public Slider getTrackSlider() {
        return trackSlider;
    }
    
    public Button getIncButton() {
        return incButton;
    }
    
    public Button getDecButton() {
        return decButton;
    }
    
    public float getScrollSpace() {
        return scrollSpace;
    }
        
    public void scrollByUnit(int direction) {
        if (direction!=INCREMENT&&direction!=DECREMENT)
            throw new IllegalArgumentException("must scroll by INCREMENT or DECREMENT");
        float size = getScrollSpace();
        if (getOrientation()==VERTICAL) 
            direction *= -1;
        setValue(getValue() + (direction*size));
    }
    
    public boolean isMouseWheelEnabled() {
        return mouseWheelEnabled;
    }

    public void setMouseWheelEnabled(boolean mouseWheelEnabled) {
        this.mouseWheelEnabled = mouseWheelEnabled;
    }
    
    public void setValue(float value) {
        getTrackSlider().setValue(value);
    }
    
    public float getValue() {
        return getTrackSlider().getValue();
    }
    
    protected void onDecrement() {
        scrollByUnit(DECREMENT);
    }
    
    protected void onIncrement() {
        scrollByUnit(INCREMENT);
    }
    
    protected void onTrackChange() {
    }
    
    public int getOrientation() {
        return getTrackSlider().getOrientation();
    }
    
    protected void renderComponent(Graphics g) {
        super.renderComponent(g);
        
    }
    
    protected void onResize() {
        if (getOrientation()==HORIZONTAL) {
            decButton.x.set(getLeft(), true);
            incButton.x.set(getWidth()-getRight()-incButton.getWidth(), true);
            trackSlider.x.set(decButton.getX()+decButton.getWidth(), true);
            trackSlider.width.set(getWidth()-getLeft()-getRight()-incButton.getWidth()-decButton.getWidth(), true);
        }
    }
    
    protected void onMouseWheelMove(int amount) {
        if (isMouseWheelEnabled()) {
            int direction = amount>0 ? INCREMENT : DECREMENT;
            scrollByUnit(direction);
        }
    }
    
    protected void updateComponent(int delta) {
        super.updateComponent(delta);
               
        repeatTimer.update(getDesktop().getContext(), delta);
        
        if (repeat!=0 && repeatTimer.isAction()) {
            if (repeat==INCREMENT)
                onIncrement();
            else
                onDecrement();
        }
    }
    
    protected void onThemeChange() {
        ComponentTheme ui = getTheme();
        if (ui!=null) {
            OxyDoc doc = getDesktop().getDoc();
            ComponentTheme btnTheme = ui.getChild("decButton");
            if (btnTheme==null)
                btnTheme = SkinDoc.getDefaultTheme(doc, "button");
            getDecButton().setTheme(btnTheme);
            
            btnTheme = ui.getChild("incButton");
            if (btnTheme==null)
                btnTheme = SkinDoc.getDefaultTheme(doc, "button");
            getIncButton().setTheme(btnTheme);
            
            ComponentTheme sliderTheme = ui.getChild("trackSlider");
            if (sliderTheme==null) {
                boolean hrz = getOrientation()==HORIZONTAL;
                sliderTheme = SkinDoc.getDefaultTheme(doc, hrz ? "hSlider" : "vSlider");
            }
            getTrackSlider().setTheme(sliderTheme);
        }
    }
}
