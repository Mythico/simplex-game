/*
 * ScrollPane.java
 *
 * Created on March 31, 2008, 9:10 PM
 */

package mdes.oxy;

/**
 *
 * @author davedes
 */
public class ScrollPane extends Panel {
    
    private ScrollBar horizBar;
    private ScrollBar vertBar;
    private Panel panel;
    
    /** Creates a new instance of ScrollPane */
    public ScrollPane(Desktop desktop) {
        super(desktop);
        horizBar = new ScrollBar(desktop, ScrollBar.HORIZONTAL) {
            
        };
        vertBar = new ScrollBar(desktop, ScrollBar.VERTICAL) {
            
        };
        
    }
    
}
