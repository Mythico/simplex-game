/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
 * @author Samuel
 */
public class EditorState extends BasicGameState {

    private final int stateId;
    private int width = 16;
    private int height = 16;
    private NodeFactory nodeFactory;
    private Node selectedStartNode;
    private Node selectedEndNode;

    private enum Type {
        None, Factory, Dummy, Connection;
    }

    // map between the area and whether it is click-through or not
    private Map<MouseOverArea, Boolean> bars = new HashMap<MouseOverArea, Boolean>();

    private List<Entity> entities = new LinkedList<>();
    private List<MouseOverArea> placers = new LinkedList<>();

    private Type selectedType = Type.None;
    private Image selectedImage = null;
    private MouseOverArea placeFactory;
    private MouseOverArea placeDummy;
    private MouseOverArea placeConnection;
    private MouseOverArea sideBar;
    private MouseOverArea bottomBar;

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
                        - ImageManager.sidebar.getHeight() / 2, barListener);

        bottomBar = new MouseOverArea(gc, ImageManager.bottombar, gc.getWidth()
                / 2 - ImageManager.bottombar.getWidth() / 2, gc.getHeight()
                - ImageManager.bottombar.getHeight(), barListener);

        bottomBar.setMouseOverImage(ImageManager.bottombarMouseOver);

        bars.put(sideBar, false);
        bars.put(bottomBar, true);

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

        placers.add(placeFactory);
        placers.add(placeDummy);
        placers.add(placeConnection);

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

        for (Entity entity : entities)
            entity.render(g);
        
        for (MouseOverArea bar : bars.keySet())
            bar.render(gc, g);

        for (MouseOverArea placer : placers)
            placer.render(gc, g);

        if (!selectedType.equals(Type.None)) {
            g.drawImage(selectedImage, gc.getInput().getAbsoluteMouseX(), gc
                    .getInput().getAbsoluteMouseY());
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        
        Set<Entry<MouseOverArea, Boolean>> barSet = bars.entrySet();
        Iterator<Entry<MouseOverArea, Boolean>> it = barSet.iterator();

        while (it.hasNext()) {
            Map.Entry<MouseOverArea, Boolean> bar = it.next();
            MouseOverArea barArea = bar.getKey();

            boolean clickThrough = bar.getValue();
            if (clickThrough)
                barArea.removeListener(barListener);
            else
                barArea.addListener(barListener);
        }
        
        for (Entity entity : entities)
            entity.update(delta);

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

        if (coordInBar(x, y))
            return;

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
                if (selectedEndNode == selectedStartNode)
                    selectedEndNode = selectedStartNode = null;
                else if (selectedEndNode != null) {
                    Connection c = new Connection();
                    nodeFactory.bind(selectedStartNode, selectedEndNode, c);
                    entities.add(c);
                    selectedEndNode = selectedStartNode = null;
                }
            }
        }
    }

    /**
     * Check if coordinates is in any of the bars
     * @param x coordinates in grid format, horizontal
     * @param y coordinates in grid format, vertical
     * @return whether the coordinates are in any of the bars or not
     */
    private boolean coordInBar(int x, int y) {
        // TODO: change this ugly? solution
        boolean inBar = false;
        Set<Entry<MouseOverArea, Boolean>> b = bars.entrySet();
        Iterator<Entry<MouseOverArea, Boolean>> it = b.iterator();

        while (it.hasNext()) {
            Map.Entry<MouseOverArea, Boolean> m = it.next();
            MouseOverArea bar = m.getKey();

            boolean clickThrough = m.getValue();
            if (clickThrough)
                continue;

            if ((x > bar.getX()) && (x < bar.getX() + bar.getWidth())
                    && (y < bar.getY() + bar.getHeight()) && (y > bar.getY()))
                inBar = true;
        }
        return inBar;
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
                if (nodeCoords[0] == coords[0] && nodeCoords[1] == coords[1])
                    n = (Node) entity;
            }
        }
        return n;
    }

    private ComponentListener barListener = new ComponentListener() {
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
