package simplex;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mdes.oxy.Button;
import mdes.oxy.Desktop;
import mdes.oxy.OxyException;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import simplex.level.LevelFileHandler;
import simplex.util.GridConversions;

/**
 * The state used when selecting which level to play.
 *
 * @author Emil
 * @author Samuel
 */
public class NextGameState extends BaseState {

    public NextGameState(int stateId) {
        super(stateId);
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.enter(gc, sbg);
        reloadGUI(gc);
    }

    @Override
    protected Desktop loadGui(GameContainer gc) throws SlickException {
        createGui();
        try {
            return Desktop.parse(NextGameState.this, gc, "gui/LevelSelectGui.xml");
        } catch (OxyException e) {
            throw new SlickException(e.getMessage(), e);
        }

    }

    /**
     * Create the GUI for selecting levels.
     * Generates and appends XML-code to the GUI panel.
     */
    private void createGui() {
        File levelFolder = new File("level");
        String[] files = levelFolder.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.indexOf(".yml") != -1;
            }
        });
        StringBuilder panel = new StringBuilder();
        panel.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        panel.append("<desktop>\n");
        panel.append("<panel name=\"TopBar\" location=\"0%, 0%\" "
                + "opaque=\"true\" background=\"#f0f0f0\" "
                + "size=\"100%, 30px\">\n");
        panel.append("<button name=\"menu1_btn\" text=\"Back\" "
                + "location=\"0px, 3px\" action=\"goToMain()\" />\n");
        panel.append("</panel>\n");
        panel.append("<panel name=\"LevelSelect\" location=\"0px,30px\" "
                + "opaque=\"true\" background=\"#f0f0f0\" "
                + "size=\"100%,100%\">\n");
        
        panel.append("<label text=\"Level Select\" location=\"5px,5px\" />\n");

        for (int i = 0; i < files.length; i++) {
            panel.append(createButton(i, files[i]));
        }
        panel.append("</panel>\n");
        panel.append("</desktop>\n");

        try (FileWriter out = new FileWriter("gui/LevelSelectGui.xml")) {
            out.write(panel.toString());
        } catch (IOException ex) {
            Logger.getLogger(NextGameState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a button for a level.
     * @param nr the number for in which order this button is placed
     * @param filename the name of the level
     * @return the XML-code for the button
     */
    private String createButton(int nr, String filename) {
        final int gameWidth = GridConversions.getGameWidth();
        final float gridWidth = GridConversions.getGridWidth();
        final float gridHeight = GridConversions.getGridHeight();

        float x = (nr % (gameWidth / 3)) * gridWidth * 3;
        float y = nr / (gameWidth / 3) * gridHeight;

        int index = filename.indexOf(".yml");

        String name = filename.substring(0, index);

        //Creates a button with the format:
        //<button name="filename" text="filename" location="xpx,ypx" 
        //action="setLevel(filename)"\>
        return "<button name=\"" + name + "\" "
                + "text=\"" + name + "\" "
                + "location=\"" + x + "px," + (30+y) + "px\" "
                + "action=\"setLevel(" + name + ")\" />\n";

    }

    /**
     * Used by the GUI to switch to main menu.
     */
    public void goToMain() {
        setNextState(Main.MAINMENUSTATE);
    }

    /**
     * Used by the GUI to switch to the corresponding level.
     * @param levelButton the button corresponding to the level.
     */
    public void setLevel(Button levelButton) {
        LevelFileHandler.instance().setLevel(levelButton.getName());
        setNextState(Main.GAMESTATE);
    }
}
