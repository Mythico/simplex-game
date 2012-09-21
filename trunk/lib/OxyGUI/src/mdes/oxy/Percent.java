/*
 * Percent.java
 *
 * Created on March 22, 2008, 1:10 PM
 */

package mdes.oxy;

/**
 * Convenience class for relative-point positions (x, y, width, heigth values).
 * @author davedes
 */
public class Percent extends Position {
    
    /**
     * A percentage as a decimal (e.g. 0.25 => 25%)
     */
    public Percent(float value) {
        super(value);
    }
    
    public Percent() {
    }
    
    public boolean isFixed() {
        return false;
    }
}
