/*
 * ComponentTest.java
 *
 * Created on March 24, 2008, 12:14 AM
 */

package mdes.oxy.test;

import mdes.oxy.*;
import org.newdawn.slick.*;

/**
 *
 * @author davedes
 */
public class ComponentTest extends BasicGame {
    
    public static void main(String[] args) throws Exception {
        AppGameContainer app = new AppGameContainer(new ComponentTest());
        app.setDisplayMode(800,600,false);
        app.start();
    }
    
    /**
     * Creates a new instance of ComponentTest
     */
    public ComponentTest() {
        super("TestComponent");
    }
    
    Desktop desktop;
    Label modalLabel;
    Dialog modalWindow;
    Button okButton;
    TextField nameField;
    
    public void init(GameContainer container) throws SlickException {
        container.getGraphics().setBackground(new Color(0.75f, 0.75f, 0.75f));
        try {
            desktop = Desktop.parse(this, container, "testdata/ComponentTest.xml");
        } catch (OxyException e) {
            e.printStackTrace();
            throw new SlickException("cannot load Oxy");
        }
        OxyDoc doc = desktop.getDoc();
        modalLabel = (Label)doc.getElement("modalLabel"); 
        okButton = (Button)doc.getElement("okButton");
        nameField = (TextField)doc.getElement("nameField");
        modalWindow = (Dialog)doc.getElement("messageBox");
    }
    
    public void modalListener(String name) {
        String str;
        if (modalWindow.isClosePressed()) {
            str = "Dialog closed";
        } else
            str = "You entered: "+name;
        modalLabel.setText(str);
        modalLabel.setVisible(true);
    }
    
    public void checkName(String name) {
        okButton.setEnabled( name.trim().length()!=0 
                    && !name.equals(nameField.getFirstText()) );
    } 
    
    public void toggleEnabled(Button left, Button right) {
        right.setEnabled(!left.isSelected());
        right.setAutoPackEnabled(false);
        right.setText(right.isEnabled() ? "Enabled" : "Disabled");
    }
    
    public void toggleModal(boolean value) {
        if (value) {
            modalLabel.setVisible(false);
        }
        modalWindow.setVisible(value);
    }
    
    public void toggleMask(boolean value) {
        nameField.setMaskEnabled(value);
    }
        
    public void update(GameContainer container, int delta) throws SlickException {
        desktop.update(delta);
        
        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE))
            container.exit();
    }
    
    public void render(GameContainer container, Graphics g) throws SlickException {
        desktop.render(g);
    }
}
