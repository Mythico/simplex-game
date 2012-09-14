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
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import simplex.entity.Connection;
import simplex.entity.Entity;
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
    private List<Entity> entities = new LinkedList<>();
    private NodeFactory nodeFactory;
    private Node selectedStartNode;
    private Node selectedEndNode;

    private enum Type {
        None, Factory, Dummy, Connection;
    }

    private Type selectedType = Type.None;
    private Image selectedImage = null;
    private MouseOverArea placeFactory;
    private MouseOverArea placeDummy;
    private MouseOverArea placeConnection;
    private MouseOverArea sideBar;

    public EditorState(int stateId) {
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

        nodeFactory = new NodeFactory();

        sideBar = new MouseOverArea(gc, ImageManager.sidebar,
                gc.getWidth() - 46, gc.getHeight() / 2
                        - ImageManager.sidebar.getHeight() / 2, sideBarListener);

        placeFactory = new MouseOverArea(gc, ImageManager.factory_node.copy(),
                gc.getWidth() - 32, sideBar.getY() + 3, placeFactoryListener);

        placeDummy = new MouseOverArea(gc, ImageManager.dummy_node.copy(),
                gc.getWidth() - 32, placeFactory.getY() + 3
                        + ImageManager.factory_node.getHeight(),
                placeDummyListener);

        placeConnection = new MouseOverArea(gc, ImageManager.connection_icon,
                gc.getWidth() - 32, placeDummy.getY() + 3
                        + ImageManager.dummy_node.getHeight(),
                placeConnectionListener);

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

        sideBar.render(gc, g);
        placeFactory.render(gc, g);
        placeDummy.render(gc, g);
        placeConnection.render(gc, g);

        for (Entity entity : entities) {
            entity.render(g);
        }

        if (!selectedType.equals(Type.None)) {
            g.drawImage(selectedImage, gc.getInput().getAbsoluteMouseX(), gc
                    .getInput().getAbsoluteMouseY());
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        for (Entity entity : entities) {
            entity.update(delta);
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

        // TODO: change this ugly? solution for checking if mouse is released in
        // the sidebar
        if ((x > sideBar.getX()) && (y > sideBar.getY())
                && (y < sideBar.getY() + sideBar.getHeight())) {
            return;
        }

        int[] coords = GridConversions.screenToGridCoord(x, y);

        if (selectedType.equals(Type.Dummy)) {
            Node node = nodeFactory.createDummyNode(coords[0], coords[1]);
            entities.add(node);
            
        } else if (selectedType.equals(Type.Factory)) {
            Node factory = nodeFactory
                    .createFactory(coords[0], coords[1], 0, 1);
            entities.add(factory);
            
        } else if (selectedType.equals(Type.Connection)) {
            if (selectedStartNode == null) {
                selectedStartNode = getNode(coords);
            } else if (selectedEndNode == null) {
                selectedEndNode = getNode(coords);
                Connection c = new Connection();
                nodeFactory.bind(selectedStartNode, selectedEndNode, c);
                entities.add(c);
                selectedEndNode = selectedStartNode = null;
            }
        }
    }

    /**
     * Get the node at the specified coordinates.
     * @param coords grid coordinates for where you want to find a node
     * @return the node at the coordinates, null if none was found
     */
    private Node getNode(int[] coords) {
        Node n = null;
        for (Entity entity : entities) {
            if (entity.getClass().equals(Node.class)) {
                int[] nodeCoords = GridConversions.screenToGridCoord(
                        (int) ((Node) entity).getPosition().x,
                        (int) ((Node) entity).getPosition().y);
                if (nodeCoords[0] == coords[0] && nodeCoords[1] == coords[1]) {
                    n = (Node) entity;
                }
            }
        }
        return n;
    }

    private ComponentListener sideBarListener = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent source) {
            selectedType = Type.None;
            selectedImage = null;
        }
    };

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

    private ComponentListener placeConnectionListener = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent source) {
            if (selectedType.equals(Type.None)) {
                selectedType = Type.Connection;
                selectedImage = ImageManager.connection_icon;
            } else {
                selectedType = Type.None;
                selectedImage = null;
            }
        }
    };
}
