package simplex;


import mdes.oxy.Button;
import mdes.oxy.Desktop;
import mdes.oxy.OxyException;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import simplex.level.LevelFileHandler;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class NextTutorialState extends NextGameState {
    
    private int currentTutorial = 1;

    public NextTutorialState(int stateId) {
        super(stateId);
        
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.enter(gc, sbg);
        
        getGuiComponent("Tutorial_1").setVisible(false);
        getGuiComponent("Tutorial_2").setVisible(false);
        getGuiComponent("Tutorial_3").setVisible(false);
        getGuiComponent("Tutorial_" + currentTutorial).setVisible(true);
    }

    @Override
    protected Desktop loadGui(GameContainer gc) throws SlickException {
        try {
            return Desktop.parse(NextTutorialState.this, gc, "gui/TutorialGui.xml");
        } catch (OxyException e) {
            throw new SlickException(e.getMessage(), e);
        }
    }

    @Override
    public void setLevel(Button tutorialLevelButton) {         
        currentTutorial++;
        if(currentTutorial > 3) {
            currentTutorial = 1;
        }
        LevelFileHandler.instance().setLevel(tutorialLevelButton.getName());
        setNextState(Main.TUTORIAL);
    }
}
