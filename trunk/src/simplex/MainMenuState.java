/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Emil
 */
public class MainMenuState extends BasicGameState {

    private final int stateId;
    private int selectedOption = Main.MAINMENUSTATE; //Do nothing

    public MainMenuState(int stateId) {
        this.stateId = stateId;
    }
       
    
    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {
        
        
        
        g.drawString("Play", 10, 100);
        g.drawString("Quit", 10, 115);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) 
            throws SlickException {
        
        if(selectedOption != Main.MAINMENUSTATE){
            sbg.enterState(selectedOption);
        }
        
    }
    
    @Override
    public void mouseReleased(int button, int x, int y){
        Point point = new Point(x, y);
        
        Rectangle rec = new Rectangle(10, 100, 50, 15);
        if(rec.contains(point))
        {
            selectedOption = Main.GAMESTATE;
        } 
        rec = new Rectangle(10, 115, 50, 15);
        if (rec.contains(point)){
            System.exit(0);
        }
    }
    
}
