/*
 * Dialog.java
 *
 * Created on March 25, 2008, 2:10 PM
 */

package mdes.oxy;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author davedes
 */
public class Dialog extends Window {
    
    private Button closeButton;
    private Label titleLabel;
    private Panel contentPane;
    protected boolean rootPaneCheckingEnabled = true;
    
    private boolean draggable = true;
    
    private RenderState active;
    private RenderState inactive;
    
    public float c_xOff, c_yOff, c_xOff2, c_yOff2;
    public float t_xOff, t_yOff, t_xOff2;
    
    private boolean exit = false;
    
    /** Creates a new instance of Dialog */
    public Dialog(Desktop desktop) {
        super(desktop);
        closeButton = new Button(desktop) {
            protected void onAction() {
                Dialog.this.setVisible(false, true);
            }
        };
        titleLabel = new Label(desktop) {
            
            private int lastX, lastY;
    
            protected void onMouseDrag(int button, int x, int y, int mouseX, int mouseY) {
                if (!isDraggable() || button!=0)
                    return;
                
                Dialog.this.x.set(Dialog.this.x.get(false) + mouseX-lastX, true);
                Dialog.this.y.set(Dialog.this.y.get(false) + mouseY-lastY, true);

                lastX = mouseX;
                lastY = mouseY;
            }

            protected void onMousePress(int button, int x, int y, int mouseX, int mouseY) {
                lastX = mouseX;
                lastY = mouseY;
            }
        };
        titleLabel.setAutoPackEnabled(false);
        contentPane = new Panel(desktop);
        super.add(contentPane);
        super.add(titleLabel);
        super.add(closeButton);
    }
        
    public void setContentOffsets(float xOff, float yOff, float xOff2, float yOff2) {
        this.c_xOff = xOff;
        this.c_yOff = yOff;
        this.c_xOff2 = xOff2;
        this.c_yOff2 = yOff2;
    }
    
    public void setTitleOffsets(float xOff, float yOff, float xOff2) {
        this.t_xOff = xOff;
        this.t_yOff = yOff;
        this.t_xOff2 = xOff2;
    }
        
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {
        if (bean == "title") {
            setTitle(value);
            return true;
        } else
            return super.handleSpecialSetter(bean, value);
    }
    
    public void setVisible(boolean visible) {
        setVisible(visible, false);
    }
    
    private void setVisible(boolean visible, boolean exit) {
        if (!visible)
            this.exit = exit;
        super.setVisible(visible);
    }
    
    /**
     * Returns <tt>true</tt> if the last call to hide the
     * dialog was initiated by the closeButton.
     * 
     * @return <tt>true</tt> if the dialog was closed 
     */
    public boolean isClosePressed() {
        return exit;
    }
    
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    public Button getCloseButton() {
        return closeButton;
    }
    
    public Label getTitleLabel() {
        return titleLabel;
    }
    
    public Panel getContentPane() {
        return contentPane;
    }
        
    public void setCloseButtonVisible(boolean b) {
        getCloseButton().setVisible(b);
    }
    
    public boolean isCloseButtonVisible() {
        return getCloseButton().isVisible();
    }
    
    public void add(Component child) {
        if (rootPaneCheckingEnabled)
            contentPane.add(child);
        else
            super.add(child);
    }
   
    public boolean remove(Component child) {
        if (rootPaneCheckingEnabled)
            return contentPane.remove(child);
        else
            return super.remove(child);
    }
        
    protected void renderComponent(Graphics g) {        
        super.renderComponent(g);
        ComponentTheme ui = getTheme();
        if (ui!=null) {
            RenderState r = inactive;
            if (isActive())
                r = active;
            ui.render(this, r);
        }
    }
    
    private void updateOffsets() {
        float w = width.get();
        float h = height.get();
        
        contentPane.x.set(c_xOff, true);
        contentPane.y.set(c_yOff, true);
        contentPane.width.set(w-c_xOff2-c_xOff, true);
        contentPane.height.set(h-c_yOff2-c_yOff, true);
        titleLabel.x.set(t_xOff, true);
        titleLabel.y.set(t_yOff, true);
        titleLabel.width.set(w-t_xOff2-t_xOff, true);
    }
    
    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }
    
    protected void onResize() {
        updateOffsets();
    }
    
    protected void onThemeChange() {
        ComponentTheme ui = getTheme();
        if (ui!=null) {
            inactive = ui.getState("inactive");
            active = ui.getState("active");
            if (active==null)
                active = inactive;
            
            //setup controls (child themes)
            OxyDoc doc = getDesktop().getDoc();
            ComponentTheme btnTheme = ui.getChild("closeButton");
            if (btnTheme==null)
                btnTheme = SkinDoc.getDefaultTheme(doc, "button");
            getCloseButton().setTheme(btnTheme);
            
            ComponentTheme labelTheme = ui.getChild("titleLabel");
            if (labelTheme==null)
                labelTheme = SkinDoc.getDefaultTheme(doc, "label");
            getTitleLabel().setTheme(labelTheme);
            getTitleLabel().setAutoPackEnabled(false);
            
            ComponentTheme contentTheme = ui.getChild("contentPanel");
            if (contentTheme==null)
                contentTheme = SkinDoc.getDefaultTheme(doc, "panel");
            getContentPane().setTheme(contentTheme);
                        
            updateOffsets();
        }
    }
}
