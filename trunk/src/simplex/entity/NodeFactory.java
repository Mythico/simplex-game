/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import simplex.util.GridConversions;

/**
 *
 * @author Emil
 */
public class NodeFactory {

    private Node createNode(int x, int y, NodeSpecification spec) {
        return new Node(GridConversions.gridToScreenCoord(x, y), spec);
    }

    public Node createFactory(int x, int y, int type, int rate) {
        NodeSpecification spec = new FactorySpecification(type, rate);
        return createNode(x, y, spec);
    }

    public Node createDummyNode(int x, int y) {
        NodeSpecification spec = new DummySpecification();
        return createNode(x, y, spec);
    }

    public Node createConsumerNode(int x, int y, int type, int rate) {
        NodeSpecification spec = new ConsumerSpecification(type, rate);
        return createNode(x, y, spec);
    }

    public Node createEaterNode(int x, int y, int fraction) {
        NodeSpecification spec = new EaterSpecification(fraction);
        return createNode(x, y, spec);
    }
    
    public Node createSplitterNode(int x, int y) {
        NodeSpecification spec = new SplitterSpecification();
        return createNode(x, y, spec);
    }

    /**
     * Binds n1 and n2 with conn.
     *
     * @param n1 Start node
     * @param n2 End node
     * @param conn The connection
     */
    public void bind(Node n1, Node n2, Connection conn) {
        conn.setStartPos(n1.getPosition());
        conn.setEndPos(n2.getPosition());
        n1.getNodeSpecification().addOutgoingConnection(conn);
        n2.getNodeSpecification().addIncomingConnection(conn);
    }
}