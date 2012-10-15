package simplex;

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
            throw new SlickException("Can't load gui");
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
                nodes.put(coords, pickedNode);
                pickedNode = null;
            } else if (connection != null && selectedNode != null) {
                Node node = nodes.get(coords);
                if (node != null) {
                    connection.bind(selectedNode, node);
                    connections.add(connection);
                    connection = null;
                    unselect();
                }
            } else if (nodes != null) {
                selectNode(nodes.get(coords));
                if (connection != null) {
                    connection.setStartNode(selectedNode);
                }
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
        NodeSpecification spec = null;
        if (selectedNode != null) {
            selectedNode.setSelected(true);
            spec = selectedNode.getNodeSpecification();
        }
        setNodeGui(spec);
    }

    private void unselect() {
        if (selectedNode != null) {
            selectedNode.setSelected(false);
            selectedNode = null;
        }
    }

    public void spawnFactory() {
        pickedNode = NodeFactory.instance().createFactoryNode();
    }

    public void spawnConsumer() {
        pickedNode = NodeFactory.instance().createConsumerNode();
    }

    public void spawnEater() {
        pickedNode = NodeFactory.instance().createEaterNode();
    }

    public void spawnSplitter() {
        pickedNode = NodeFactory.instance().createSplitterNode();
    }

    public void spawnConnection() {
        connection = new Connection();
        connection.setStartNode(NodeFactory.instance().createDummyNode());
        connection.setEndNode(NodeFactory.instance().createDummyNode());
    }

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
     * GUI function.
     * Saves a level to disk.
     * @param filename The name of the level that will be saved.
     */
    public void save(String filename) {
        System.out.println("Saveing");
        LevelFileHandler lfh = LevelFileHandler.instance();
        lfh.saveLevel(level, filename);
        getGuiComponent("MenuPanel").setVisible(false);
    }

    /**
     * GUI function.
     * Loads a level from disk.
     * @param filename The name of the level that will be loaded.
     */
    public void load(String filename) {
        System.out.println("Loading");
        LevelFileHandler lfh = LevelFileHandler.instance();
        level = lfh.loadLevel(filename);
        getGuiComponent("MenuPanel").setVisible(false);
    }

    public void toggleVisible(Panel panel) {
        panel.setVisible(!panel.isVisible());
    }

    public void showMenu(String name) {
        Panel p = getGuiComponent("MenuPanel");
        Button b = getGuiComponent("menu_btn");
        TextField f = getGuiComponent("menu_field");

        p.setVisible(!p.isVisible() || !b.getText().equalsIgnoreCase(name));
        
        b.setText(Character.toUpperCase(name.charAt(0)) + name.substring(1));
        b.setAction(name + "(menu_field.text)");
        f.setAction(name + "(menu_field.text)");
    }

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

    private void setNodeGui(final NodeSpecification spec) {

        Label label = getGuiComponent("nodeLabel");
        Label label1 = getGuiComponent("label1");
        Label label2 = getGuiComponent("label2");
        Spinner spinner1 = getGuiComponent("spinner1");
        Spinner spinner2 = getGuiComponent("spinner2");
        Button button = getGuiComponent("btn");

        if (spec instanceof FactorySpecification) {
            label.setText("Factory");
            label1.setText("Resource Rate");
            label2.setText("Resource Type");
            label1.setVisible(true);
            label2.setVisible(true);
            spinner1.setVisible(true);
            spinner2.setVisible(true);
            button.setVisible(true);
        } else if (spec instanceof ConsumerSpecification) {
            label.setText("Consumer");
            label1.setText("Resource Rate");
            label2.setText("Resource Type");
            label1.setVisible(true);
            label2.setVisible(true);
            spinner1.setVisible(true);
            spinner2.setVisible(true);
            button.setVisible(true);
        } else if (spec instanceof EaterSpecification) {
            label.setText("Eater");
            label1.setText("Fraction");
            label1.setVisible(true);
            label2.setVisible(false);
            spinner1.setVisible(true);
            spinner2.setVisible(false);
            button.setVisible(true);
        } else if (spec instanceof SplitterSpecification) {
            label.setText("Splitter");
            label1.setVisible(false);
            label2.setVisible(false);
            spinner1.setVisible(false);
            spinner2.setVisible(false);
            button.setVisible(true);
        } else {
            label.setText("Select a node");
            spinner1.setVisible(false);
            spinner2.setVisible(false);
            label1.setVisible(false);
            label2.setVisible(false);
            button.setVisible(false);
        }
    }

    /**
     * Set the text on the escape button.
     * Also move the other buttons to match the escape button new size.
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
