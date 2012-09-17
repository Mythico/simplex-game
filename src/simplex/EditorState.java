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
import simplex.util.GridCoord;
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
    private List<Connection> connections = new LinkedList<>();
    private Node pickedNode;
    private Node selectedNode;
    private Connection connection;
    private HashMap<GridCoord, Node> nodes = new HashMap<>();

    // map between the area and whether it is click-through or not
    private Map<MouseOverArea, Boolean> bars = new HashMap<>();
    private List<MouseOverArea> placers = new LinkedList<>();
    private MouseOverArea placeFactory;
    private MouseOverArea placeConsumer;
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

        nodeFactory = NodeFactory.instance();

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
        
        placeConsumer = new MouseOverArea(gc, ImageManager.consumer_node.copy(),
                gc.getWidth() - 32, placeFactory.getY() + 3
                + ImageManager.factory_node.getHeight(),
                placeConsumerListener);

        placeDummy = new MouseOverArea(gc, ImageManager.dummy_node.copy(),
                gc.getWidth() - 32, placeConsumer.getY() + 3
                + ImageManager.consumer_node.getHeight(),
                placeDummyListener);

        placeConnection = new MouseOverArea(gc, ImageManager.connection_icon,
                gc.getWidth() - 32, placeDummy.getY() + 3
                + ImageManager.dummy_node.getHeight(),
                placeConnectionListener);

        placers.add(placeFactory);
        placers.add(placeConsumer);
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

        for (Node node : nodes.values()) {
            node.render(g);
        }
        for (Entity conn : connections) {
            conn.render(g);
        }

        for (MouseOverArea bar : bars.keySet()) {
            bar.render(gc, g);
        }

        for (MouseOverArea placer : placers) {
            placer.render(gc, g);
        }

        GridCoord coord = GridConversions.mouseToGridCoord(
                gc.getInput().getAbsoluteMouseX(),
                gc.getInput().getAbsoluteMouseY());
        if (pickedNode != null) {
            pickedNode.setPosition(GridConversions.gridToScreenCoord(coord));
            pickedNode.render(g);
        } else if (connection != null && selectedNode != null) {
            connection.setStartPos(selectedNode.getPosition());
            connection.setEndPos(GridConversions.gridToScreenCoord(coord));
            connection.render(g);
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
            if (clickThrough) {
                barArea.removeListener(barListener);
            } else {
                barArea.addListener(barListener);
            }
        }

        for (Node node : nodes.values()) {
            node.update(delta);
        }
        for (Entity connection : connections) {
            connection.update(delta);
        }

        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            if (pickedNode != null) {
                pickedNode = null;
            } else if (connection != null) {
                connection = null;
            } else if (selectedNode != null) {
                unselect();
            } else {
                sbg.enterState(Main.MAINMENUSTATE);
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {

        if (coordInBar(x, y)) {
            return;
        }

        GridCoord coords = GridConversions.mouseToGridCoord(x, y);

        if (Input.MOUSE_LEFT_BUTTON == button) {
            if (pickedNode != null) {
                nodes.put(coords, pickedNode);
                pickedNode = null;
            } else if (connection != null && selectedNode != null) {
                Node node = nodes.get(coords);
                if (node != null) {
                    nodeFactory.bind(selectedNode, node, connection);
                    connections.add(connection);
                    connection = null;
                    unselect();
                }
            } else {
                selectNode(nodes.get(coords));
            }
        } else if (Input.MOUSE_RIGHT_BUTTON == button) {
            selectedNode = pickedNode = nodes.remove(coords);
        }
    }

    private void selectNode(Node n) {
        unselect();
        selectedNode = n;
        if (selectedNode != null) {
            selectedNode.setSelected(true);
        }
    }

    private void unselect() {
        if (selectedNode != null) {
            selectedNode.setSelected(false);
            selectedNode = null;
        }
    }

    /**
     * Check if coordinates is in any of the bars
     *
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
            if (clickThrough) {
                continue;
            }

            if ((x > bar.getX()) && (x < bar.getX() + bar.getWidth())
                    && (y < bar.getY() + bar.getHeight()) && (y > bar.getY())) {
                inBar = true;
            }
        }
        return inBar;
    }
    private ComponentListener barListener = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent source) {
        }
    };
    private ComponentListener placeFactoryListener = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent source) {
            pickedNode = nodeFactory.createFactoryNode();
        }
    };
    private ComponentListener placeConsumerListener = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent source) {
            pickedNode = nodeFactory.createConsumerNode();
        }
    };
    private ComponentListener placeDummyListener = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent source) {
            pickedNode = nodeFactory.createDummyNode();
        }
    };
    private ComponentListener placeConnectionListener = new ComponentListener() {
        @Override
        public void componentActivated(AbstractComponent source) {
            connection = new Connection();
        }
    };
}
