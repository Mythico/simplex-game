/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.util.GridConversions;
import simplex.util.ImageManager;

/**
 *
 * @author Emil
 */
public class EditorState extends BasicGameState {

    private final int stateId;
    private int width = 16;
    private int height = 16;
    private List<Node> factories = new LinkedList<>();
    private List<Node> nodes = new LinkedList<>();
    private NodeFactory nodeFactory;

    private enum Type {

	None, Factory, Dummy;
    }
    private Type selectedType = Type.None;
    private Image selectedImage = null;
    private MouseOverArea placeFactory;
    private MouseOverArea placeDummy;

    public EditorState(int stateId) {
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
	nodeFactory = new NodeFactory();


	placeFactory = new MouseOverArea(gc, ImageManager.factory_node.copy(),
		gc.getWidth() - 32, 0,
		placeFactoryListener);

	placeDummy = new MouseOverArea(gc, ImageManager.dummy_node.copy(),
		gc.getWidth() - 32, 32,
		placeDummyListener);

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

	g.setColor(Color.white);
	g.fillRect(gc.getWidth() - 35, 0, 35, gc.getHeight());
	g.setColor(Color.black);
	g.drawRect(gc.getWidth() - 35, 0, 35, gc.getHeight());
	placeFactory.render(gc, g);
	placeDummy.render(gc, g);

	for (Node node : nodes) {
	    node.render(g);
	}

	if (!selectedType.equals(Type.None)) {
	    g.drawImage(selectedImage, gc.getInput().getAbsoluteMouseX(),
		    gc.getInput().getAbsoluteMouseY());
	}
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
	    throws SlickException {

	for (Node node : factories) {
	    node.update(delta);
	}

	if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
	    if (selectedType.equals(Type.None)) {
		sbg.enterState(Main.MAINMENUSTATE);
	    } else {
		selectedType = Type.None;
		selectedImage = null;
	    }
	}
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
	int[] coords = GridConversions.screenToGridCoord(x, y);
	
	if(selectedType.equals(Type.Dummy)){
	    Node node = nodeFactory.createDummyNode(coords[0], coords[1]);
	    nodes.add(node);
	} else if(selectedType.equals(Type.Factory)){
	    Node factory = nodeFactory.createFactory(coords[0], coords[1], 0, 1);
	    factories.add(factory);
	    nodes.add(factory);
	}
    }
    
    
    
    private ComponentListener placeFactoryListener = new ComponentListener() {
	@Override
	public void componentActivated(AbstractComponent source) {
	    if (selectedType.equals(Type.None)) {
		selectedType = Type.Factory;
		selectedImage = ImageManager.factory_node;
	    } else {
		selectedType = Type.None;
		selectedImage = null;
	    }
	}
    };
    private ComponentListener placeDummyListener = new ComponentListener() {
	@Override
	public void componentActivated(AbstractComponent source) {
	    if (selectedType.equals(Type.None)) {
		selectedType = Type.Dummy;
		selectedImage = ImageManager.dummy_node;
	    } else {
		selectedType = Type.None;
		selectedImage = null;
	    }
	}
    };
}
