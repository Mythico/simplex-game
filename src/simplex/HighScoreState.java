package simplex;

import mdes.oxy.Desktop;
import mdes.oxy.OxyException;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A states that will draw the high scores you have achieved.
 *
 * @author Emil
 * @author Samuel
 */
public class HighScoreState extends BaseState {
    
    public HighScoreState(int stateId) {
        super(stateId);
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.enter(gc, sbg);
        HighScore.load();
    }

    @Override
    protected Desktop loadGui(GameContainer gc) throws SlickException {
        try {
            return Desktop.parse(HighScoreState.this, gc, "gui/HighScoreGui.xml");
        } catch (OxyException e) {
            throw new SlickException(e.getMessage(), e);
        }

    }


    @Override
    protected void renderContent(GameContainer container, StateBasedGame game, Graphics g) {
        HighScore.render(g);
    }
    
    /**
     * A function that will be called from the back button defined in the GUI.
     */
    public void goToMain(){
        setNextState(Main.MAINMENUSTATE);
    }
    

    /**
     * GUI function. Clears the highscore.
     */
    public void clear(){
        HighScore.clear();
    }
}
