/*
 * TextArea.java
 *
 * Created on May 14, 2008, 4:17 PM
 */

package mdes.oxy;

import org.newdawn.slick.Font;
import org.newdawn.slick.Input;

/**
 *
 * @author davedes
 */
public class TextArea extends TextComponent {
    
    private int cols, rows;
    
    /** Creates a new instance of TextArea */
    public TextArea(Desktop desktop) {
        super(desktop);
        
    }
    
    protected int getMaxUndoStates() {
        return 0;
    }
    
    public void updateCols(int cols) {
        this.cols = cols;
        Font f = getFont();
        String text = getText();
        if (f!=null) {
            float w = 0;
            float oneCol = f.getWidth(COL_CHAR);
            if (cols>0) {
                w = getLeft() + oneCol * cols + getRight()+2;
            }
            width.set(w, true);
        }
    }
    
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {        
        if (bean == "cols") {
            try { cols = Integer.parseInt(value); }
            catch (NumberFormatException e) { 
                throw new OxyException("cannot parse cols with "+value); 
            }
            return true;
        } else if (bean == "rows") {
            try { rows = Integer.parseInt(value); }
            catch (NumberFormatException e) { 
                throw new OxyException("cannot parse rows with "+value); 
            }
            return true;
        } else 
            return super.handleSpecialSetter(bean, value);
    }
    
    
}
