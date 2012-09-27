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
public class NextGameState extends BasicGameState {

    private final int stateId;
    private int selectedOption = Main.NEXT_GAME; // Do nothing

    private MouseOverArea doneButton;

    public NextGameState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {

        Image doneImage = new Image("img/done.png");
        doneButton = new MouseOverArea(gc, doneImage, 10, 100, doneClicked);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {

        doneButton.render(gc, g);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i)
            throws SlickException {

        if (selectedOption != Main.NEXT_GAME) {
            sbg.enterState(selectedOption);
        }

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
        selectedOption = Main.NEXT_GAME;
        super.enter(container, game);
    }

    private ComponentListener doneClicked = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent ac) {
            selectedOption = Main.MAINMENUSTATE;
        }
    };


}
