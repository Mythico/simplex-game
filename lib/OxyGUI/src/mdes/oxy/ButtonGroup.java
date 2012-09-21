/*
 * ButtonGroup.java
 *
 * Created on March 24, 2008, 6:52 PM
 */

package mdes.oxy;

import java.util.ArrayList;

/**
 *
 * @author davedes
 */
public class ButtonGroup {
    
    private Button selected;
    private ArrayList buttons = new ArrayList();
    
    /** Creates a new instance of ButtonGroup */
    public ButtonGroup() {
    }
    
    public void add(Button btn) {
        if (btn.group==this)
            return;
        if (btn.group!=null)
            btn.group.remove(btn);
        btn.group = this;
        buttons.add(btn);
        if (btn.selectedFlag)
            setSelected(btn, true);
    }
    
    public void remove(Button btn) {
        boolean b = buttons.remove(btn);
        if (b) {
            if (isSelected(btn))
                selected = null;
            btn.group = null;
        }
    }
    
    protected void selectNewButton(Button btn) {
        if (selected!=null)
            selected.selectedFlag = false;
        selected = btn;
        if (selected!=null)
            selected.selectedFlag = true;
    }
        
    public void setSelected(Button btn, boolean val) {
        boolean isSel = isSelected(btn);
        
        if (val && !isSel) { //select if necessary
            selectNewButton(btn);
        } else if (!val && isSel) { //deselect if necessary
            selectNewButton(null);
        }
    }
    
    public boolean isSelected(Button btn) {
        return selected == btn;
    }
    
    
}
