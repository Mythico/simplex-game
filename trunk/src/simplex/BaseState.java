package simplex;

import mdes.oxy.Component;
import mdes.oxy.Desktop;
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
    private boolean quit;

    public BaseState(int stateId) {
        this.stateId = stateId;
        nextState = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        reloadGUI(gc);
    }

    protected abstract Desktop loadGui(GameContainer gc) throws SlickException;

    protected void reloadGUI(GameContainer gc) throws SlickException{
        if(desktop != null){
            desktop.setVisible(false);
        }
        desktop = loadGui(gc);
        if(desktop != null){
            desktop.setVisible(true);
        }
    }
    
    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        desktop.setVisible(true);
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        nextState = stateId;
        desktop.setVisible(false);
    }
    
    

    /**
     * A helper method to find Gui components in the document.
     *
     * @param <T>
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends Component> T getGuiComponent(String name) {
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
            StateBasedGame game, Graphics g) {
    }

    protected void renderContent(GameContainer container,
            StateBasedGame game, Graphics g) {
    }

    protected void renderForeground(GameContainer container,
            StateBasedGame game, Graphics g) {
        desktop.render(g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        if (quit) {
            gc.exit();
        }
        if (nextState != stateId) {
            sbg.enterState(nextState);
        }
        desktop.update(delta);
    }

    protected void setNextState(int state) {
        nextState = state;
    }

    protected void setQuit() {
        quit = true;
    }
}
