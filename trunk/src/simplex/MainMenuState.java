/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * @author Emil
 */
public class MainMenuState extends BasicGameState {

    private final int stateId;
    private int selectedOption = Main.MAINMENUSTATE; // Do nothing

    private MouseOverArea playButton;
    private MouseOverArea editButton;
    private MouseOverArea quitButton;

    public MainMenuState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {

        Image playImage = new Image("img/play.png");
        Image editImage = new Image("img/edit.png");
        Image quitImage = new Image("img/quit.png");
        playButton = new MouseOverArea(gc, playImage, 10, 100, playClicked);
        editButton = new MouseOverArea(gc, editImage, 10, 164, editClicked);
        quitButton = new MouseOverArea(gc, quitImage, 10, 228, quitClicked);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {

        playButton.render(gc, g);
        editButton.render(gc, g);
        quitButton.render(gc, g);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i)
            throws SlickException {

        if (selectedOption != Main.MAINMENUSTATE) {
            sbg.enterState(selectedOption);
        }

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
        selectedOption = Main.MAINMENUSTATE;
        super.enter(container, game);
    }

    private ComponentListener playClicked = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent ac) {
            selectedOption = Main.GAMESTATE;
        }
    };

    private ComponentListener editClicked = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent ac) {
            selectedOption = Main.EDITSTATE;
        }
    };

    private ComponentListener quitClicked = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent ac) {
            System.exit(0);
        }
    };

}
