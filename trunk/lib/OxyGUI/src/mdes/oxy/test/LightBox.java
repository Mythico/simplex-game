/*
 * LightBox.java
 *
 * Created on April 9, 2008, 8:13 PM
 */

package mdes.oxy.test;

import mdes.oxy.Desktop;
import mdes.oxy.Percent;
import mdes.oxy.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author davedes
 */
public class LightBox extends Window {
    
    public static final int BORDER_THICKNESS = 5;
    
    private Image image;
    
    /** Creates a new instance of LightBox */
    public LightBox(Desktop desktop) {
        super(desktop);
        setVisible(false);
        setOpaque(true);
        setBackground(new Color(Color.white));
        setCenteringEnabled(true);
        setLocation(new Percent(0.5f), new Percent(0.5f));
        setTop(5);
        setLeft(5);
        setRight(5);
        setBottom(25);
    }
    
    protected void onMousePress(int button, int x, int y, int mouseX, int mouseY) {
        super.onMousePress(button, x, y, mouseX, mouseY);
        if (button==0)
            setVisible(false);
    }
    
    public void showImage(Image image) {
        this.image = image;
        width.set(image.getWidth()+getLeft()+getRight(), true);
        height.set(image.getHeight()+getTop()+getBottom(), true);
        setVisible(true);
    }

    protected void renderComponent(Graphics g) {
        super.renderComponent(g);
        float abx = (int)getXOnScreen();
        float aby = (int)getYOnScreen();
        float w = getWidth();
        float h = getHeight();

        if (image!=null) {
            float x = abx+(w/2f - image.getWidth()/2f);
            float y = aby+getTop();
            g.drawImage(image, x, y);
            g.setColor(getForeground());
            Font f = getFont();
            g.setFont(f);
            g.drawString("Click to close", x+getLeft(), aby+h-getBottom()/2f-f.getLineHeight()/2f);
        }
    }
}
