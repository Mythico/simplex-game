/*
 * Position.java
 *
 * Created on March 22, 2008, 1:08 PM
 */

package mdes.oxy;

/**
 * A fixed-point position (for x, y, width, heigth values).
 * @author davedes
 */
public class Position {
    
    float val = 0;
    
    /**
     * Creates a new instance of Position
     */
    public Position(float val) {
        this.val = val;
    }
    
    public Position() {
    }
    
    public float getValue() {
        return val;
    }
    
    public void setValue(float val) {
        this.val = val;
    }
    
    public boolean isFixed() {
        return true;
    }
}
