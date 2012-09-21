/*
 * Calculator.java
 *
 * Created on March 31, 2008, 9:10 PM
 */

package mdes.oxy.test;

import java.text.DecimalFormat;
import mdes.oxy.*;
import org.newdawn.slick.*;

/**
 *
 * @author davedes
 */
public class Calculator extends BasicGame {
    
    public static void main(String[] args) throws Exception {
        AppGameContainer app = new AppGameContainer(new Calculator());
        app.setDisplayMode(800,600,false);
        app.start();
    }
    
    /**
     * Creates a new instance of Calculator
     */
    public Calculator() {
        super("Calculator");
    }
    
    Desktop desktop;
    
    public void init(GameContainer container) throws SlickException {
        container.getGraphics().setBackground(new Color(0.75f, 0.75f, 0.75f));
        try {
            desktop = Desktop.parse(this, container, "testdata/Calculator.xml");
        } catch (OxyException e) {
            e.printStackTrace();
            throw new SlickException("cannot load Oxy");
        }
    }
    
    public void calculate(int left, int right, TextField result) {        
        String val = DecimalFormat.getInstance().format(left+right);
        result.setText(val);
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
