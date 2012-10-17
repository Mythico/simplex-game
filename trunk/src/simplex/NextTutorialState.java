package simplex;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mdes.oxy.Button;
import mdes.oxy.Desktop;
import mdes.oxy.OxyException;
import org.newdawn.slick.*;

import simplex.entity.specification.FactorySpecification;
import simplex.level.LevelFileHandler;
import simplex.util.ImageManager;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class NextTutorialState extends NextGameState {
    
    private boolean initiated = false;

    public NextTutorialState(int stateId) {
        super(stateId);
    }

    @Override
    protected Desktop loadGui(GameContainer gc) throws SlickException {
        if (!initiated)
            initTutorialGui();
        try {
            return Desktop.parse(NextTutorialState.this, gc, "gui/TutorialGui.xml");
        } catch (OxyException e) {
            throw new SlickException(e.getMessage(), e);
        }
    }
    
    private void initTutorialGui() {
        StringBuilder panel = new StringBuilder();
        panel.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        panel.append("<desktop>\n");
        panel.append("<panel name=\"Tutorial\" location=\"0px,0px\" "
                + "opaque=\"true\" background=\"#f0f0f0\" "
                + "size=\"100%,100%\">\n");
        panel.append("<label text=\"Tutorial: Factory and Consumer\" location=\"155px,55px\" />\n\n");
        panel.append("<label text=\"Click the arrow on the connection between\" location=\"160px,85px\" />\n");
        panel.append("<label text=\"the factory and the consumer to allow the\" location=\"160px,105px\" />\n");
        panel.append("<label text=\"resources to move.\" location=\"160px,125px\" />\n");   
        //panel.append("<label image=\"ImageManager.get(new FactorySpecification())\" location=\"190px,145px\" />\n");
        
        panel.append("<button name=\"tutorial1\" "
                + "text=\"Start Tutorial\" location=\"150px,190px\" "
                + "action=\"setLevel(tutorial1)\" />\n");
        panel.append("</panel>\n");
        panel.append("</desktop>\n");

        try (FileWriter out = new FileWriter("gui/TutorialGui.xml")) {
            out.write(panel.toString());
        } catch (IOException ex) {
            Logger.getLogger(NextGameState.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        initiated = true;
    }

    @Override
    public void setLevel(Button tutorialLevelButton) {
        LevelFileHandler.instance().setLevel(tutorialLevelButton.getName());
        setNextState(Main.TUTORIAL);
    }
}
