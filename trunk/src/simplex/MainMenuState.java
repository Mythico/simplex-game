package simplex;

import mdes.oxy.Desktop;
import mdes.oxy.OxyException;
import org.newdawn.slick.*;

/**
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

    public void goToLevel() {
        setNextState(Main.NEXT_GAME);
    }

    public void goToEditor() {
        setNextState(Main.EDITSTATE);
    }

    public void goToScore() {
        setNextState(Main.HIGH_SCORE);
    }

    public void goToTutorial() {
        setNextState(Main.NEXT_TUTORIAL);
    }

    public void goToQuit() {
        setQuit();
    }
}
