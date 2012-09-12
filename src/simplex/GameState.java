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
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.util.GridConversions;

/**
 *
 * @author Emil
 */
public class GameState extends BasicGameState {

    private final int stateId;
    private int width = 16;
    private int height = 16;
    private List<Node> factories = new LinkedList<>();
    private NodeFactory nodeFactory;

    public GameState(int stateId) {
	this.stateId = stateId;
    }

    @Override
    public int getID() {
	return stateId;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {


	 
	GridConversions.setGameSize(width, height);
	GridConversions.setScreenSize(gc.getWidth(), gc.getHeight());
	
	NodeFactory f = new NodeFactory();

	Node n1 = f.createFactory(1, 2, 0, 1);
	Node n2 = f.createDummyNode(3, 2);
	Node n3 = f.createDummyNode(2, 5);
	Node n4 = f.createDummyNode(5, 5);



	Connection c1 = new Connection(n2);
	Connection c2 = new Connection(n3);
	Connection c3 = new Connection(n4);
	Connection c4 = new Connection(n4);

	n1.addConnection(c1);
	n2.addConnection(c2);
	n3.addConnection(c3);
	n2.addConnection(c4);

	factories.add(n1);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
	    throws SlickException {

	final int gwidth = gc.getWidth() / width;
	final int gheight = gc.getHeight() / height;

	//Draw grid
	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		//Toggle colour
		g.setColor(((i + j) % 2 == 0) ? Color.darkGray : Color.gray);
		g.fillRect(j * gwidth, i * gheight, gwidth, gheight);
	    }
	}

	for (Node node : factories) {
	    node.render(g);
	}
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
	    throws SlickException {

	for (Node node : factories) {
	    node.update(delta);
	}
    }
}
