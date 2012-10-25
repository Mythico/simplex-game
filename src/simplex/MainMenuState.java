package simplex;

import mdes.oxy.Desktop;
import mdes.oxy.OxyException;
import org.newdawn.slick.*;

/**
 * The state for the main menu, contains GUI functions 
 * for changing to different states.
 *
 * @author Emil
 * @author Samuel
 */
public class MainMenuState extends BaseState {

    public MainMenuState(int stateId) {
        super(stateId);
    }

    @Override
    protected Desktop loadGui(GameContainer gc) throws SlickException {
        try {
            return Desktop.parse(MainMenuState.this, gc, "gui/MainMenuGui.xml");
        } catch (OxyException e) {
            System.err.println(e);
            throw new SlickException("Can't load gui");
        }
    }

    /**
     * GUI function for changing to the level-select state.
     */
    public void goToLevel() {
        setNextState(Main.NEXT_GAME);
    }

    /**
     * GUI function for changing to the editor state.
     */
    public void goToEditor() {
        setNextState(Main.EDITSTATE);
    }

    /**
     * GUI function for changing to the highscore state.
     */
    public void goToScore() {
        setNextState(Main.HIGH_SCORE);
    }

    /**
     * GUI function for changing to the tutorial.
     */
    public void goToTutorial() {
        setNextState(Main.NEXT_TUTORIAL);
    }

    /**
     * GUI function for quitting the game.
     */
    public void goToQuit() {
        setQuit();
    }
}
