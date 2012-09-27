/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import simplex.entity.Connection;
import simplex.entity.ConsumerSpecification;
import simplex.entity.EaterSpecification;
import simplex.entity.FactorySpecification;
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.NodeSpecification;
import simplex.entity.Resource;
import simplex.util.LevelFileHandler;
import simplex.util.GridConversions;
import simplex.util.GridCoord;
import simplex.util.ImageManager;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class GameState extends BasicGameState {

    private final int stateId;
    private int width = 16;
    private int height = 16;
    private Map<GridCoord, Node> nodes = new HashMap<>();
    private List<Connection> connections = new LinkedList<>();
    private List<MouseOverArea> tempConnSwap = new LinkedList<>();
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
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        nodes = LevelFileHandler.loadNodesFromFile("nodes.yml");
        connections = LevelFileHandler.loadConnectionsFromFile("connections.yml");
        
        if (nodes.isEmpty()) {

            GridConversions.setGameSize(width, height);
            GridConversions.setScreenSize(gc.getWidth(), gc.getHeight());
            GridCoord p1 = new GridCoord(1, 2);
            GridCoord p2 = new GridCoord(5, 2);
            GridCoord p3 = new GridCoord(1, 5);
            GridCoord p4 = new GridCoord(5, 5);

            NodeFactory nodeFactory = NodeFactory.instance();
            Node n1 = nodeFactory.createFactoryNode();
            n1.setPosition(GridConversions.gridToScreenCoord(p1));
            ((FactorySpecification) n1.getNodeSpecification()).setResource(
                    new Resource(Color.red, 4), null);
            Node n2 = nodeFactory.createSplitterNode();
            n2.setPosition(GridConversions.gridToScreenCoord(p2));
            Node n3 = nodeFactory.createEaterNode();
            n3.setPosition(GridConversions.gridToScreenCoord(p3));
            ((EaterSpecification) n3.getNodeSpecification()).setFraction(2);
            Node n4 = nodeFactory.createConsumerNode();
            n4.setPosition(GridConversions.gridToScreenCoord(p4));
            ((ConsumerSpecification) n4.getNodeSpecification())
                    .setExpectedResource(new Resource(Color.red, 1));

            Connection c1 = new Connection();
            Connection c2 = new Connection();
            Connection c3 = new Connection();
            Connection c4 = new Connection();


            nodeFactory.bind(n1, n2, c1);
            nodeFactory.bind(n2, n3, c2);
            nodeFactory.bind(n3, n4, c3);
            nodeFactory.bind(n2, n4, c4);

            connections.add(c1);
            connections.add(c2);
            connections.add(c3);
            connections.add(c4);

            nodes.put(p1, n1);
            nodes.put(p2, n2);
            nodes.put(p3, n3);
            nodes.put(p4, n4);
        }

        tempConnSwap.clear();
        for (Connection conn : connections) {
            Vector2f middle = conn.getStartNode().getPosition();
            tempConnSwap.add(new MouseOverArea(gc,
                    ImageManager.connection_swap_icon,
                    (int) middle.x, (int) middle.y));
            conn.swapDirection();
        }
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


        for (Connection connection : connections) {
            connection.render(g);
        }

        for (Node node : nodes.values()) {
            node.render(g);
        }

        for (MouseOverArea moa : tempConnSwap) {
            moa.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        if (levelIsDone()) {
            nextState = Main.NEXT_GAME;
        }

        if (delta == 0) {
            return;
        }

        for (Node node : nodes.values()) {
            node.update(delta);
        }
        for (Connection connection : connections) {
            connection.update(delta);
        }

        if (nextState != Main.GAMESTATE) {
            int state = nextState;
            nextState = Main.GAMESTATE;
            sbg.enterState(state);
        }

        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            sbg.enterState(Main.MAINMENUSTATE);
        }


        if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            for (int i = 0; i < tempConnSwap.size(); i++) {
                if (tempConnSwap.get(i).isMouseOver()) {
                    connections.get(i).swapDirection();
                }
            }
        }

    }

    @Override
    public void keyReleased(int key, char c) {
        if (Input.KEY_P == key || Input.KEY_PAUSE == key) {
            gc.setPaused(!gc.isPaused());
        }
    }

    private boolean levelIsDone() {
        //TODO: do a better check.
        for (Node node : nodes.values()) {
            NodeSpecification spec = node.getNodeSpecification();
            if (spec instanceof ConsumerSpecification && !((ConsumerSpecification) spec).isHappy()) {
                return false;
            }
        }
        return true;
    }
}
