/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import simplex.World;

/**
 *
 * @author Emil
 */
public class Display {
    
    
    public Display(World world){
        
    }

    void render(GameContainer gc, Graphics g) throws SlickException{
        g.drawString("Woop Woop", 100, 100);
    }
}
