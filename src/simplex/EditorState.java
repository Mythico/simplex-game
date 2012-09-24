package simplex;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

import mdes.oxy.Component;
import mdes.oxy.Desktop;
import mdes.oxy.Label;
import mdes.oxy.OxyDoc;
import mdes.oxy.OxyException;
import mdes.oxy.Spinner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import simplex.entity.Connection;
import simplex.entity.ConsumerSpecification;
import simplex.entity.EaterSpecification;
import simplex.entity.Entity;
import simplex.entity.FactorySpecification;
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.NodeSpecification;
import simplex.entity.Resource;
import simplex.entity.SplitterSpecification;
import simplex.util.FileHandler;
import simplex.util.GridConversions;
import simplex.util.GridCoord;

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
    private Node pickedNode;
    private Node selectedNode;
    private Connection connection;
    private Map<GridCoord, Node> nodes = new HashMap<>();
    private List<Connection> connections = new LinkedList<>();
    private Desktop desktop;
    private File levelFile = new File("level.yml");

    public EditorState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {

        try {
            desktop = Desktop.parse(this, gc, "gui/EditorGui.xml");
        } catch (OxyException e) {
            System.err.println(e);
            throw new SlickException("Can't load Editor gui");
        }
        setNodeGui(null); // Set no selected node

        GridConversions.setGameSize(width, height);
        GridConversions.setScreenSize(gc.getWidth(), gc.getHeight());

        nodeFactory = NodeFactory.instance();

        Iterable<Object> levelIter = FileHandler.loadFromFile(levelFile);
        if (levelIter != null) {
            for (Object o : levelIter) {
                if (o != null) {
                    if (nodes.isEmpty()) {
                        nodes = (Map<GridCoord, Node>) o;
                    } else {
                        connections = (List<Connection>) o;
                    }
                }
            }
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

        if (nodes != null) {
            for (Node node : nodes.values()) {
                node.render(g);
            }
        }
        if (connections != null) {
            for (Entity conn : connections) {
                conn.render(g);
            }
        }

        GridCoord coord = GridConversions.mouseToGridCoord(gc.getInput()
                .getAbsoluteMouseX(), gc.getInput().getAbsoluteMouseY());
        if (pickedNode != null) {
            pickedNode.setPosition(GridConversions.gridToScreenCoord(coord));
            pickedNode.render(g);
        } else if (connection != null && selectedNode != null) {
            // Node node = nodes.get(coord);
            // connection.render(g);
            // TODO: add a proxy connection?
        }

        desktop.render(g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        desktop.update(delta);

        if (nodes != null) {
            for (Node node : nodes.values()) {
                node.update(delta);
            }
        }
        if (connections != null) {
            for (Entity conn : connections) {
                conn.update(delta);
            }
        }

        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            if (pickedNode != null) {
                pickedNode = null;
            } else if (connection != null) {
                connection = null;
            } else if (selectedNode != null) {
                unselect();
            } else {
                List<Object> level = new LinkedList<Object>();
                level.add(nodes);
                level.add(connections);
                FileHandler.saveToFile(level.iterator(), levelFile);
                sbg.enterState(Main.MAINMENUSTATE);
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {

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
            } else if (nodes != null) {
                selectNode(nodes.get(coords));
            } else {
                selectNode(null);
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
            final NodeSpecification spec = selectedNode.getNodeSpecification();
            setNodeGui(spec);
        }
    }

    private void unselect() {
        if (selectedNode != null) {
            selectedNode.setSelected(false);
            selectedNode = null;
        }
    }

    public void spawnFactory() {
        pickedNode = nodeFactory.createFactoryNode();
    }

    public void spawnConsumer() {
        pickedNode = nodeFactory.createConsumerNode();
    }

    public void spawnEater() {
        pickedNode = nodeFactory.createEaterNode();
    }

    public void spawnSplitter() {
        pickedNode = nodeFactory.createSplitterNode();
    }

    public void spawnConnection() {
        connection = new Connection();
    }

    public void setNodeData(int data1, int data2) {

        if (selectedNode == null) {
            return;
        }

        final NodeSpecification spec = selectedNode.getNodeSpecification();
        if (spec instanceof FactorySpecification) {
            Resource r = Resource.parse(data2, data1);
            ((FactorySpecification) spec).setResource(r, null);
        } else if (spec instanceof ConsumerSpecification) {
            Resource r = Resource.parse(data2, data1);
            ((ConsumerSpecification) spec).setExpectedResource(r);
        } else if (spec instanceof EaterSpecification) {
            ((EaterSpecification) spec).setFraction(data1);
        }
    }

    private void setNodeGui(final NodeSpecification spec) {

        final OxyDoc doc = desktop.getDoc();
        Label label = (Label) doc.getElement("nodeLabel");
        Label label1 = (Label) doc.getElement("label1");
        Label label2 = (Label) doc.getElement("label2");
        Spinner spinner1 = (Spinner) doc.getElement("spinner1");
        Spinner spinner2 = (Spinner) doc.getElement("spinner2");
        if (spec instanceof FactorySpecification) {
            label.setText("Factory");
            label1.setText("Resource Rate");
            label2.setText("Resource Type");
            spinner1.setVisible(true);
            spinner2.setVisible(true);
        } else if (spec instanceof ConsumerSpecification) {
            label.setText("Consumer");
            label1.setText("Resource Rate");
            label2.setText("Resource Type");
            spinner1.setVisible(true);
            spinner2.setVisible(true);
        } else if (spec instanceof EaterSpecification) {
            label.setText("Eater");
            label1.setText("Fraction");
            label2.setText("");
            spinner1.setVisible(true);
            spinner2.setVisible(false);
        } else if (spec instanceof SplitterSpecification) {
            label.setText("Splitter");
            label1.setText("");
            label2.setText("");
            spinner1.setVisible(false);
            spinner2.setVisible(false);
        } else {
            spinner1.setVisible(false);
            spinner2.setVisible(false);
        }
        ((Component) doc.getElement("btn")).setVisible(true);
    }
}
