/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Emil
 */
public class NextGameState extends BaseState {

    private MouseOverArea doneButton;

    public NextGameState(int stateId) {
        super(stateId);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {

        Image doneImage = new Image("img/done.png");
        doneButton = new MouseOverArea(gc, doneImage, 10, 100, doneClicked);
    }

    @Override
    protected void renderBackground(GameContainer gc, StateBasedGame sbg,
            Graphics g) {
    }

    @Override
    protected void renderContent(GameContainer gc, StateBasedGame sbg,
            Graphics g) {
        doneButton.render(gc, g);
    }

    @Override
    protected void renderForeground(GameContainer gc, StateBasedGame sgb,
            Graphics g) {
    }
    private ComponentListener doneClicked = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent ac) {
            setNextState(Main.MAINMENUSTATE);
        }
    };
}
