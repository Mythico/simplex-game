/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import org.newdawn.slick.geom.Vector2f;
import simplex.util.GridConversions;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 */
public class NodeFactory {

    private static NodeFactory nodeFactory;

    private NodeFactory() {}

    public static NodeFactory instance() {
        if (nodeFactory == null) {
            nodeFactory = new NodeFactory();
        }
        return nodeFactory;
    }

    public Node createFactoryNode() {
        return new Node(new FactorySpecification());
    }

    public Node createDummyNode() {
        return new Node(new DummySpecification());
    }

    public Node createConsumerNode() {
        return new Node(new ConsumerSpecification());
    }

    public Node createEaterNode() {
        return new Node(new EaterSpecification());
    }

    public Node createSplitterNode() {
        return new Node(new SplitterSpecification());
    }

    /**
     * Finds a node between to positions and bind a connection between them.
     *
     * @param n1 Start startPosition
     * @param n2 End position
     * @param conn The connection
     */
    public void bind(Node n1, Node n2, Connection conn) {
        conn.setStartPos(n1.getPosition());
        conn.setEndPos(n2.getPosition());
        n1.getNodeSpecification().addOutgoingConnection(conn);
        n2.getNodeSpecification().addIncomingConnection(conn);
    }

}