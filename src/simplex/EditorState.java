package simplex;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mdes.oxy.Button;
import mdes.oxy.Desktop;
import mdes.oxy.Label;
import mdes.oxy.OxyException;
import mdes.oxy.Panel;
import mdes.oxy.Position;
import mdes.oxy.Spinner;
import mdes.oxy.TextField;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import simplex.entity.Connection;
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.Resource;
import simplex.entity.specification.CombinerSpecification;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.FactorySpecification;
import simplex.entity.specification.NodeSpecification;
import simplex.entity.specification.SplitterSpecification;
import simplex.level.LevelFileHandler;
import simplex.util.GridConversions;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class EditorState extends EngineState {

    private Node pickedNode;
    private Node selectedNode;
    private Connection connection;

    public EditorState(int stateId) {
        super(stateId);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {
        super.init(gc, sbg);
        setNodeGui(null); // Set no selected node
        getGuiComponent("MenuPanel").setVisible(false);
    }

    @Override
    protected Desktop loadGui(GameContainer gc) throws SlickException {
        try {
            return Desktop.parse(EditorState.this, gc, "gui/EditorGui.xml");
        } catch (OxyException e) {
            System.err.println(e);
            throw new SlickException("Can't load the gui.");
        }
    }

    @Override
    public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {
        super.renderContent(gc, sbg, g);

        if (pickedNode != null) {
            pickedNode.render(g);
        } else if (connection != null && selectedNode != null) {
            connection.render(g);
        }

        if (pickedNode != null) {
            setEscapeButton("Remove Node");
        } else if (connection != null) {
            setEscapeButton("Remove Connection");
        } else if (selectedNode != null) {
            setEscapeButton("Unselect Node");
        } else {
            setEscapeButton("Back");
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        super.update(gc, sbg, delta);

        GridCoord coord = GridConversions.mouseToGridCoord(gc.getInput()
                .getAbsoluteMouseX(), gc.getInput().getAbsoluteMouseY());
        if (pickedNode != null) {
            pickedNode.setGridPosition(coord);
        } else if (connection != null && selectedNode != null) {
            connection.getEndNode().setGridPosition(coord);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        if (Input.KEY_ESCAPE == key) {
            escape();
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {

        GridCoord coords = GridConversions.mouseToGridCoord(x, y);
        Map<GridCoord, Node> nodes = level.getNodes();
        List<Connection> connections = level.getConnections();
        if (Input.MOUSE_LEFT_BUTTON == button) {
            if (pickedNode != null) {
                // Place node:
                nodes.put(coords, pickedNode);
                selectNode(pickedNode);
                pickedNode = null;
            } else if (connection != null && selectedNode != null) {
                // Bind connection:
                Node node = nodes.get(coords);
                if (node != null) {
                    connection.bind(selectedNode, node);
                    connections.add(connection);
                    connection = null;
                    unselect();
                }
            } else if (nodes != null) {
                // Select node:
                selectNode(nodes.get(coords));
                if (connection != null && selectedNode != null) {
                    connection.setStartNode(selectedNode);
                }
            } else {
                // Unselect:
                selectNode(null);
                unselect();
            }
        } else if (Input.MOUSE_RIGHT_BUTTON == button) {
            // If a node is deleted:
            if (pickedNode != null) {
                Iterator<Connection> i = connections.iterator();
                while (i.hasNext()) {
                    Connection conn = i.next();
                    if (conn.isConnectedTo(pickedNode)) {
                        i.remove();
                    }
                }
            }
            // Pick a node:
            selectedNode = pickedNode = nodes.remove(coords);
        }
    }

    /**
     * Select the specified node.
     * @param n the node to select.
     */
    private void selectNode(Node n) {
        unselect();
        selectedNode = n;
        NodeSpecification spec = null;
        if (selectedNode != null) {
            selectedNode.setSelected(true);
            spec = selectedNode.getNodeSpecification();
        }
        setNodeGui(spec);
    }

    /**
     * If a node is selected, unselect it.
     */
    private void unselect() {
        if (selectedNode != null) {
            selectedNode.setSelected(false);
            selectedNode = null;
        }
    }

    /**
     * Spawn a factory
     */
    public void spawnFactory() {
        pickedNode = NodeFactory.instance().createFactoryNode();
    }

    /**
     * Spawn a consumer
     */
    public void spawnConsumer() {
        pickedNode = NodeFactory.instance().createConsumerNode();
    }

    /**
     * Spawn a eater
     */
    public void spawnEater() {
        pickedNode = NodeFactory.instance().createEaterNode();
    }

    /**
     * Spawn a splitter
     */
    public void spawnSplitter() {
        pickedNode = NodeFactory.instance().createSplitterNode();
    }
    
    /**
     * Spawn a combiner
     */
    public void spawnCombiner() {
        pickedNode = NodeFactory.instance().createCombinerNode();
    }

    /**
     * Spawn a connection
     */
    public void spawnConnection() {
        connection = new Connection();
        Node startNode = NodeFactory.instance().createDummyNode();
        if(selectedNode != null){
            float x = selectedNode.getPosition().x;
            float y = selectedNode.getPosition().y;
            startNode.setX(x);
            startNode.setY(y);
        }
        connection.setStartNode(startNode);
        connection.setEndNode(NodeFactory.instance().createDummyNode());
    }

    /**
     * Either drop or deselect a node or go to the mainmenu,
     * depending on what the user is doing.
     */
    public void escape() {
        if (pickedNode != null) {
            level.removeNode(pickedNode);
            pickedNode = null;
        } else if (connection != null) {
            connection = null;
        } else if (selectedNode != null) {
            unselect();
        } else {
            setNextState(Main.MAINMENUSTATE);
        }
    }

    /**
     * GUI function. Saves a level to disk.     *
     * @param filename The name of the level that will be saved.
     */
    public void save(String filename) {
        LevelFileHandler lfh = LevelFileHandler.instance();
        lfh.saveLevel(level, filename);
        getGuiComponent("MenuPanel").setVisible(false);
    }

    /**
     * GUI function. Loads a level from disk.     *
     * @param filename The name of the level that will be loaded.
     */
    public void load(String filename) {
        LevelFileHandler lfh = LevelFileHandler.instance();
        level = lfh.loadLevel(filename);
        getGuiComponent("MenuPanel").setVisible(false);
    }

    /**
     * Toggle the visibility of the panel
     * @param panel
     */
    public void toggleVisible(Panel panel) {
        panel.setVisible(!panel.isVisible());
    }

    /**
     * GUI function. Shows the save/load menu.
     * @param name The name of the file specified in the textfield.
     */
    public void showMenu(String name) {
        Panel p = getGuiComponent("MenuPanel");
        Button b = getGuiComponent("menu_btn");
        TextField f = getGuiComponent("menu_field");

        p.setVisible(!p.isVisible() || !b.getText().equalsIgnoreCase(name));

        b.setText(Character.toUpperCase(name.charAt(0)) + name.substring(1));
        b.setAction(name + "(menu_field.text)");
        f.setAction(name + "(menu_field.text)");
    }

    /**
     * Set the data on the selected node depending on what type it is. 
     * @param data1 resource rate if factory or consumer, fraction if eater
     * @param data2 resource type if factory or consumer
     */
    public void setNodeData(int data1, int data2) {

        if (selectedNode == null) {
            return;
        }

        final NodeSpecification spec = selectedNode.getNodeSpecification();
        if (spec instanceof FactorySpecification) {
            Resource r = Resource.parse(data2, data1);
            ((FactorySpecification) spec).setResource(r);
        } else if (spec instanceof ConsumerSpecification) {
            Resource r = Resource.parse(data2, data1);
            ((ConsumerSpecification) spec).setExpectedResource(r);
        } else if (spec instanceof EaterSpecification) {
            ((EaterSpecification) spec).setFraction(data1);
        }
    }

    /**
     * Set the GUI of the bottom panel to match the specification provided.
     * @param spec the specification to match
     */
    private void setNodeGui(final NodeSpecification spec) {

        Label label = getGuiComponent("nodeLabel");
        Label label1 = getGuiComponent("label1");
        Label label2 = getGuiComponent("label2");
        Spinner spinner1 = getGuiComponent("spinner1");
        Spinner spinner2 = getGuiComponent("spinner2");
        Button button = getGuiComponent("btn");

        //Set the default the visibility.
        spinner1.setVisible(false);
        spinner2.setVisible(false);
        label1.setVisible(false);
        label2.setVisible(false);
        button.setVisible(true);

        //Show the correct gui elements depending on what type the 
        //provided specification is. 
        if (spec instanceof FactorySpecification) {
            Resource r = ((FactorySpecification) spec).getResource();
            label.setText("Factory");
            label1.setText("Resource Rate");
            label2.setText("Resource Type");
            label1.setVisible(true);
            label2.setVisible(true);
            spinner1.setVisible(true);
            spinner1.setValue(r.getRate());
            spinner2.setVisible(true);
            spinner2.setValue(r.getType());
        } else if (spec instanceof ConsumerSpecification) {
            Resource r = ((ConsumerSpecification) spec).getExpectedResource();
            label.setText("Consumer");
            label1.setText("Resource Rate");
            label2.setText("Resource Type");
            label1.setVisible(true);
            label2.setVisible(true);
            spinner1.setVisible(true);
            spinner1.setValue(r.getRate());
            spinner2.setVisible(true);
            spinner2.setValue(r.getType());
        } else if (spec instanceof EaterSpecification) {
            int fraction = ((EaterSpecification) spec).getFraction();
            label.setText("Eater");
            label1.setText("Fraction");
            label1.setVisible(true);
            spinner1.setVisible(true);
            spinner1.setValue(fraction);
        } else if (spec instanceof SplitterSpecification) {
            label.setText("Splitter");
        } else if (spec instanceof CombinerSpecification) {
            label.setText("Combiner");
        } else {
            label.setText("Select a node");
            button.setVisible(false);
        }
    }

    /**
     * Set the text on the escape button. Also move the other buttons to match
     * the escape button new size.     *
     * @param text
     */
    private void setEscapeButton(String text) {
        Button b1 = getGuiComponent("menu1_btn");
        b1.setText(text);
        Button b2 = getGuiComponent("menu2_btn");
        b2.setX(new Position(b1.getX() + b1.getWidth()));
        Button b3 = getGuiComponent("menu3_btn");
        b3.setX(new Position(b2.getX() + b2.getWidth()));
    }
}
