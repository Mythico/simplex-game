/*
 * KnightTimes.java
 *
 * Created on April 5, 2008, 1:44 PM
 */

package mdes.oxy.test;

import org.newdawn.slick.*;
import mdes.oxy.*;
import org.newdawn.slick.util.Log;

/**
 *
 * @author davedes
 */
public class KnightTimes extends BasicGame {
    
    public static void main(String[] args) throws Exception {
        AppGameContainer app = new AppGameContainer(new KnightTimes());
        app.setDisplayMode(800,600,false);
        app.start();
    }
    
    Desktop desktop;
    Label title;
    
    /** Creates a new instance of KnightTimes */
    public KnightTimes() {
        super("KnightTimes");
    }
    
    public void init(GameContainer container) throws SlickException {
        try {
            Desktop.setSkin(new SkinDoc("testdata/knight/skin.xml"));
            desktop = Desktop.parse(this, container, "testdata/knight/layout.xml");
        } catch (OxyException e) {
            Log.error("cannot load Oxy", e);
        }
        OxyDoc doc = desktop.getDoc();
        title = (Label)doc.getElement("title");
    }
    
    public void update(GameContainer container, int delta) throws SlickException {
        //title fades in
        float scale = 0.003f;
        if (title.getAlpha() < 1f) {
            float a = title.getAlpha() + scale;
            if (a > 1f)
                a = 1f;
            title.setAlpha(a);
        }
        
        desktop.update(delta);
        
        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE))
            container.exit();
    }
    
    public void render(GameContainer container, Graphics g) throws SlickException {
        desktop.render(g);
    }
}
