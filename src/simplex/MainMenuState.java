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
public class MainMenuState extends BaseState {

    private MouseOverArea playButton;
    private MouseOverArea editButton;
    private MouseOverArea quitButton;

    public MainMenuState(int stateId) {
        super(stateId);
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
    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
        nextState = Main.MAINMENUSTATE;
    }

    private ComponentListener playClicked = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent ac) {
            nextState = Main.GAMESTATE;
        }
    };

    private ComponentListener editClicked = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent ac) {
            nextState = Main.EDITSTATE;
        }
    };

    private ComponentListener quitClicked = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent ac) {
            System.exit(0);
        }
    };

}
