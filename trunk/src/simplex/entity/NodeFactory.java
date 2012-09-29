/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import simplex.entity.specification.DummySpecification;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.SplitterSpecification;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.FactorySpecification;

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
     * @param startNode Start node
     * @param endNode End node
     * @param conn The connection
     */
    public void bind(Node startNode, Node endNode, Connection conn) {
        conn.setStartNode(startNode);
        conn.setEndNode(endNode);
    }

}