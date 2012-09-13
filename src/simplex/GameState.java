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
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import simplex.entity.Connection;
import simplex.entity.Entity;
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.util.GridConversions;

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
    private NodeFactory nodeFactory = new NodeFactory();

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

        GridConversions.setGameSize(width, height);
        GridConversions.setScreenSize(gc.getWidth(), gc.getHeight());

        Node n1 = nodeFactory.createFactory(1, 2, 1, 6);
        Node n2 = nodeFactory.createDummyNode(3, 2);
        Node n3 = nodeFactory.createEaterNode(2, 5, 2);
        Node n4 = nodeFactory.createConsumerNode(5, 5, 1, 7);

        Connection c1 = new Connection(n1.getPosition(),n2.getPosition());
        Connection c2 = new Connection(n2.getPosition(),n3.getPosition());
        Connection c3 = new Connection(n3.getPosition(),n4.getPosition());

        nodeFactory.bind(n1, n2, c1);
        nodeFactory.bind(n2, n3, c2);
        nodeFactory.bind(n3, n4, c3);

        entities.add(n1);
        entities.add(n2);
        entities.add(n3);
        entities.add(n4);
        entities.add(c1);
        entities.add(c2);
        entities.add(c3);


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

        for (Entity entity : entities) {
            entity.render(g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        for (Entity entity : entities) {
            entity.update(delta);
        }
    }
}
