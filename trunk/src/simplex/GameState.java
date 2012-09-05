/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Emil
 */
public class GameState extends BasicGameState{
    
    private final int stateId;

    public GameState(int stateId) {
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
        g.drawString("Game", 10, 100);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) 
            throws SlickException {
        
    }
}
