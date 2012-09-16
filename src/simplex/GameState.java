/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import simplex.entity.Connection;
import simplex.entity.Entity;
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.util.GridConversions;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class GameState extends BasicGameState {

    private final int stateId;
    private int width = 16;
    private int height = 16;
    private List<Entity> entities = new LinkedList<>();
    private GameContainer gc;
    private int nextState = Main.GAMESTATE;

    public GameState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {

        this.gc = gc;

        GridConversions.setGameSize(width, height);
        GridConversions.setScreenSize(gc.getWidth(), gc.getHeight());
        GridCoord p1 = new GridCoord(1, 2);
        GridCoord p2 = new GridCoord(5, 2);
        GridCoord p3 = new GridCoord(1, 5);
        GridCoord p4 = new GridCoord(5, 5);
        NodeFactory nodeFactory = NodeFactory.instance();
        nodeFactory.createFactory(p1, 1, 4);
        nodeFactory.createSplitterNode(p2);
        nodeFactory.createEaterNode(p3, 2);
        nodeFactory.createConsumerNode(p4, 1, 2);

        Connection c1 = new Connection();
        Connection c2 = new Connection();
        Connection c3 = new Connection();
        Connection c4 = new Connection();

        nodeFactory.bind(p1, p2, c1);
        nodeFactory.bind(p2, p3, c2);
        nodeFactory.bind(p3, p4, c3);
        nodeFactory.bind(p2, p4, c4);

        entities.add(c1);
        entities.add(c2);
        entities.add(c3);
        entities.add(c4);

    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {

        final int gwidth = gc.getWidth() / width;
        final int gheight = gc.getHeight() / height;

        // Draw grid
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Toggle colour
                g.setColor(((i + j) % 2 == 0) ? Color.darkGray : Color.gray);
                g.fillRect(j * gwidth, i * gheight, gwidth, gheight);
            }
        }
        NodeFactory nodeFactory = NodeFactory.instance();
        for (Entity entity : nodeFactory.getNodeList()) {
            entity.render(g);
        }
        for (Entity entity : entities) {
            entity.render(g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        if (delta == 0) {
            return;
        }
        NodeFactory nodeFactory = NodeFactory.instance();
        for (Entity entity : nodeFactory.getNodeList()) {
            entity.update(delta);
        }
        for (Entity entity : entities) {
            entity.update(delta);
        }

        if (nextState != Main.GAMESTATE) {
            int state = nextState;
            nextState = Main.GAMESTATE;
            sbg.enterState(state);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        if (Input.KEY_P == key || Input.KEY_PAUSE == key) {

            gc.setPaused(!gc.isPaused());
        }
        if(Input.KEY_ESCAPE == key){
            nextState = Main.MAINMENUSTATE;
        }
    }
}
