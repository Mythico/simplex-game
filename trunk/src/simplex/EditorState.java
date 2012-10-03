package simplex;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import mdes.oxy.Button;
import mdes.oxy.Label;
import mdes.oxy.Spinner;
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
        loadGui(gc, "EditorGui.xml");
        setNodeGui(null); // Set no selected node
    }

    @Override
    public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {
        super.renderContent(gc, sbg, g);

        if (pickedNode != null) {
            pickedNode.render(g);
        } else if (connection != null && selectedNode != null) {
            connection.render(g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        super.update(gc, sbg, delta);

        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            if (pickedNode != null) {
                level.removeNode(pickedNode);
                pickedNode = null;
            } else if (connection != null) {
                connection = null;
            } else if (selectedNode != null) {
                unselect();
            } else {
                try {
                    LevelFileHandler lfh = new LevelFileHandler();
                    lfh.saveLevel(level);
                } catch (IOException ex) {
                    Logger.getLogger(EditorState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                sbg.enterState(Main.MAINMENUSTATE);
            }
        }

        GridCoord coord = GridConversions.mouseToGridCoord(gc.getInput()
                .getAbsoluteMouseX(), gc.getInput().getAbsoluteMouseY());
        if (pickedNode != null) {
            pickedNode.setGridPosition(coord);
        } else if (connection != null && selectedNode != null) {
            connection.getEndNode().setGridPosition(coord);
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
                    NodeFactory.instance().bind(selectedNode, node, connection);
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
        connection.setEndNode(NodeFactory.instance().createDummyNode());
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
}
