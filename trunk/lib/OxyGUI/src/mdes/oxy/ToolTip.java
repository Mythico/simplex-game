/*
 * ToolTip.java
 *
 * Created on February 29, 2008, 10:31 PM
 */

package mdes.oxy;

/**
 *
 * @author davedes
 */
public class ToolTip extends Label {
    
    private Component owner;
    
    private int showDelay = 750;
    private int hideDelay = 4000;
    private float s_xOff, s_yOff=20;
    
    /**
     * Creates a new instance of ToolTip
     */
    public ToolTip(Desktop desktop) {
        super(desktop);
        setZIndex(Component.POPUP_LAYER);
    }
    
    public boolean isMouseInside() {
        Desktop d = getDesktop();
        return contains(d.getMouseX(), d.getMouseY());
    }
    
    public Component getOwner() {
        return owner;
    }

    public void setOwner(Component owner) {
        this.owner = owner;
    }
    
    public int getShowDelay() {
        return showDelay;
    }
    
    public void setShowDelay(int delay) {
        int old = showDelay;
        showDelay = delay;
        if (old!=showDelay && getDesktop().getToolTip()==this) {
            getDesktop().updateTipDelays();
        }
    }
    
    public int getHideDelay() {
        return hideDelay;
    }
    
    public void setHideDelay(int delay) {
        int old = hideDelay;
        hideDelay = delay;
        if (old!=hideDelay && getDesktop().getToolTip()==this) {
            getDesktop().updateTipDelays();
        }
    }
    
    public float getXOffset() {
        return s_xOff;
    }
    
    public float getYOffset() {
        return s_yOff;
    }
    
    public void setShowOffsets(float xOff, float yOff) {
        this.s_xOff = xOff;
        this.s_yOff = yOff;
    }
}
