package simplex;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The base class of all states in SimplexGame
 *
 * @author Emil
 * @author Samuel
 */
public abstract class BaseState extends BasicGameState {

    private final int stateId;
    private int nextState;

    public BaseState(int stateId) {
        this.stateId = stateId;
        nextState = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
        nextState = stateId;
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        
        if (nextState != stateId) {
            sbg.enterState(nextState);
        }
    }
    
    protected void setNextState(int state){
        nextState = state;
    }
}
