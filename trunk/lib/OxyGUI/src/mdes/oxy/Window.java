/*
 * Window.java
 *
 * Created on March 5, 2008, 11:25 PM
 */

package mdes.oxy;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

/**
 * A class that can be layered with others.
 * @author davedes
 */
public class Window extends Panel {
    
    private boolean alwaysOnTop = false;
    private boolean active = false;
    
    public Window(Desktop desktop) {
        super(desktop);
        setFocusable(true);
    }
        
    public void setActive(boolean active) {
        this.active = active;
        
        if (getDesktop().getModalWindow()!=this) {
            int z = alwaysOnTop ? TOP_LAYER : WINDOW_LAYER;
            if (active)
                z++;
            setZIndex(z);
        }
    }
    
    public boolean isActive() {
        return active;
    }
    
    public boolean isAlwaysOnTop() {
        return alwaysOnTop || getDesktop().getModalWindow()==this;
    }
    
    public void setVisible(boolean visible) {
        boolean old = super.isVisible();
        if (getDesktop().modalWindow==this)
            getDesktop().modalVisibilityChanged(visible, old);
        super.setVisible(visible);
    }
    
    public void setAlwaysOnTop(boolean alwaysOnTop) {    
        boolean old = this.alwaysOnTop;
        this.alwaysOnTop = alwaysOnTop;
        
        //change
        if (old != this.alwaysOnTop) {
            setZIndex(alwaysOnTop ? TOP_LAYER : WINDOW_LAYER);
        }
    }
}
