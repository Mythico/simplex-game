/*
 * ComboBox.java
 *
 * Created on March 27, 2008, 4:37 PM
 */

package mdes.oxy;

/**
 *
 * @author davedes
 */
public class ComboBox extends Panel {
    
    private Label label;
    private Button button;
        
    /** Creates a new instance of ComboBox */
    public ComboBox(Desktop desktop) {
        super(desktop);
        
        label = new Label(desktop);
        
        button = new Button(desktop) {
            protected void onAction() {
                
            }
        };
        add(label);
        add(button);
    }
    
    protected void onSizeChange() {
        label.x.set(x.get(), true);
        label.y.set(y.get(), true);
        label.width.set(width.get(), true);
        label.height.set(height.get(), true);
        button.x.set(width.get()-button.width.get()-getRight(), true);
    }
    
    
}
