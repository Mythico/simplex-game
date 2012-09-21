package mdes.oxy;
/*
 * Anchor.java
 *
 * Created on March 18, 2008, 4:23 PM
 */

import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author davedes
 */
public class Anchor {

    static final int X = 0;
    static final int Y = 1;
    static final int WIDTH = 2;
    static final int HEIGHT = 3;
    
    float value;
    private float relativeResult;
    boolean fixed;
    private boolean relativeDirty;
    private Component comp;
    private int type;
    float min = -Float.MAX_VALUE;
    float max = Float.MAX_VALUE;
    private boolean docked = false;
    
    Anchor(Component comp, int type) {
        this.comp = comp;
        this.type = type;
    }

    public boolean isFixed() {
        return fixed;
    }
    
    public float getValue() {
        return value;
    }
    
    void checkMinMax() {
        if (min > max) {
            System.out.println(min+" "+max);
            throw new IllegalArgumentException(this.toString()+" min must be less than or equal to max");
        }
    }
    
    public String toString() {
        String str = comp.getName()+" ";
        if (this==comp.x)
            str += "x";
        else if (this==comp.y)
            str += "y";
        else if (this==comp.width)
            str += "width";
        else if (this==comp.height)
            str += "height";
        return str;
    }
    
    /**
     * Returns the value as a fixed point.
     */
    public float get() {
        return get(true);
    }
    
    public float get(boolean checkCenter) {
        float p;
        if (isFixed()) {
            p = value;
        } else {
            ensureRelative();
            p = relativeResult;
        }
        if (checkCenter && (type==X||type==Y)) {
            if (type==X && comp.isCenteringX())
                p -= comp.width.get()/2f;
            else if (type==Y && comp.isCenteringY())
                p -= comp.height.get()/2f;
        }
        if (docked && (type==X || type==Y)) {
            GUIContext c = comp.getDesktop().getContext();
            Panel prt = comp.getParent();
            min = 0.0f;
            if (type==X) {
                max = (prt!=null ? prt.width.get() : c.getWidth()) - comp.width.get();
            } else 
                max = (prt!=null ? prt.height.get() : c.getHeight()) - comp.height.get();
        }
        
        return (int)(Math.max(Math.min(p, max), min));
    }
    
    protected void ensureRelative() {
        if (relativeDirty && !fixed) {
            float old = relativeResult;
            Panel p = comp.getParent();
            GUIContext c = comp.getDesktop().getContext();
            if (type==X||type==WIDTH)
                relativeResult = value * (p!=null ? p.width.get() : c.getWidth());
            else 
                relativeResult = value * (p!=null ? p.height.get() : c.getHeight());
        }
    }

    public void set(float value, boolean fixed) {
        float oldRes = get();
        
        //set new values
        float oldv = this.value;
        boolean oldf = this.fixed;
        this.value = value;
        this.fixed = fixed;
        this.relativeDirty = (oldv!=this.value || oldf!=this.fixed);
        
        float newRes = get();
        if (oldRes!=newRes) { //change
            dirty(comp, type);
        }
    }
            
    static void dirty(Component comp, final int type) {
        boolean change = type==WIDTH || type==HEIGHT;
        if (type==WIDTH) comp.width.relativeDirty = true;
        else if (type==HEIGHT) comp.height.relativeDirty = true;
                
        if (change || type==X) comp.x.relativeDirty = true;
        else if (change || type==Y) comp.y.relativeDirty = true;
        
        if (comp instanceof Panel) {
            Panel p = (Panel)comp;
            for (int i=0; i<p.getChildCount(); i++) {
                dirty(p.getChild(i), type);
            }
        }
    }    

    void setMin(float min) {
        this.min = min;
        checkMinMax();
    }

    void setMax(float max) {
        this.max = max;
        checkMinMax();
    }
}