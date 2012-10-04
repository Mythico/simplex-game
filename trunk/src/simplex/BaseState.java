package simplex;

import mdes.oxy.Component;
import mdes.oxy.Desktop;
import mdes.oxy.OxyException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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
    private Desktop desktop;

    public BaseState(int stateId) {
        this.stateId = stateId;
        nextState = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    protected void loadGui(GameContainer gc, String filename) throws SlickException {

        try {
            desktop = Desktop.parse(this, gc, "gui/" + filename);
        } catch (OxyException e) {
            System.err.println(e);
            throw new SlickException("Can't load gui");
        }
    }
    
    protected <T extends Component> T getGuiComponent(String name){        
        return (T) desktop.getDoc().getElement(name);
    }

    @Override
    public final void render(GameContainer container,
            StateBasedGame game, Graphics g) throws SlickException {
        renderBackground(container, game, g);
        renderContent(container, game, g);
        renderForeground(container, game, g);
    }

    protected void renderBackground(GameContainer container,
            StateBasedGame game, Graphics g){
        
    }

    protected void renderContent(GameContainer container,
            StateBasedGame game, Graphics g){
        
    }

    protected void renderForeground(GameContainer container,
            StateBasedGame game, Graphics g){
        if(desktop != null){
            desktop.render(g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        if (nextState != stateId) {
            //Have to reset the nextState variable or  you will
            //instantly switch to the same nextstate when entering
            //this state again.
            int temp = nextState;
            nextState = stateId;
            sbg.enterState(temp);
        }
        if(desktop != null){
            desktop.update(delta);
        }
    }

    protected void setNextState(int state) {
        nextState = state;
    }
}
