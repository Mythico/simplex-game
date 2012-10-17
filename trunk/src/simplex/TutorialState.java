package simplex;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mdes.oxy.Desktop;
import mdes.oxy.OxyException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * @author Emil
 * @author Samuel
 */
public class TutorialState extends GameState {

    private int tutorialNr = 1;

    public TutorialState(int stateId) {
        super(stateId);
    }
    
    @Override
    public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
        System.out.println("nr: " + tutorialNr);
        super.enter(gc, game);
    }

    /**
     * Used by the GUI to switch to the next level.
     */
    @Override
    public void goToNext() {
        if (tutorialNr < 3) {
            tutorialNr++;
            createTutorialGui();
            setNextState(Main.NEXT_TUTORIAL);
        } else {
            goToMain();
        }
    }

    private void createTutorialGui() {
        String buttonText = "Continue Tutorial";
        
        StringBuilder panel = new StringBuilder();
        panel.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        panel.append("<desktop>\n");
        panel.append("<panel name=\"Tutorial\" location=\"0px,0px\" "
                + "opaque=\"true\" background=\"#f0f0f0\" "
                + "size=\"100%,100%\">\n");

        switch (tutorialNr) {
        case 2:
            panel.append("<label text=\"Tutorial: Splitter\" location=\"5px,5px\" />\n\n");
            panel.append("<label text=\"The splitter node splits the incoming\" location=\"160px,85px\" />\n");
            panel.append("<label text=\"resources into the colors which together\" location=\"160px,105px\" />\n");
            panel.append("<label text=\"would make that color.\" location=\"160px,125px\" />\n");
            //panel.append("<label image=\"ImageManager.get(new SplitterSpecification())\" location=\"190px,145px\" />\n");
            break;
        case 3:
            panel.append("<label text=\"Tutorial: Eater\" location=\"155px,55px\" />\n\n");
            panel.append("<label text=\"The eater node absorbs a set fraction of the\" location=\"160px,85px\" />\n");
            panel.append("<label text=\"resources, the absorbed resources are lost.\" location=\"160px,105px\" />\n");
            //panel.append("<label image=\"ImageManager.get(new EaterSpecification())\" location=\"190px,145px\" />\n");
            break;
        }

        panel.append("<button name=\"tutorial" + tutorialNr + "\" "
                + "text=\"" + buttonText + "\" location=\"150px,190px\" "
                + "action=\"setLevel(tutorial" + tutorialNr + ")\" />\n");
        panel.append("</panel>\n");
        panel.append("</desktop>\n");

        try (FileWriter out = new FileWriter("gui/TutorialGui.xml")) {
            out.write(panel.toString());
        } catch (IOException ex) {
            Logger.getLogger(NextGameState.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
}
