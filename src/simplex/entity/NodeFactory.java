/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import java.util.Collection;
import java.util.Hashtable;
import org.newdawn.slick.geom.Vector2f;
import simplex.util.GridConversions;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 */
public class NodeFactory {

    private static NodeFactory nodeFactory;
    private Hashtable<GridCoord, Node> nodes;

    private NodeFactory() {
        nodes = new Hashtable<>();
    }

    public static NodeFactory instance() {
        if (nodeFactory == null) {
            nodeFactory = new NodeFactory();
        }
        return nodeFactory;
    }

    private void createNode(GridCoord coord, NodeSpecification spec) {
        Vector2f pos = GridConversions.gridToScreenCoord(coord);
        Node n = new Node(pos, spec);
        nodes.put(coord, n);
    }

    public void createFactory(GridCoord coord, int type, int rate) {
        NodeSpecification spec = new FactorySpecification(type, rate);
        createNode(coord, spec);
    }

    public void createDummyNode(GridCoord coord) {
        NodeSpecification spec = new DummySpecification();
        createNode(coord, spec);
    }

    public void createConsumerNode(GridCoord coord, int type, int rate) {
        NodeSpecification spec = new ConsumerSpecification(type, rate);
        createNode(coord, spec);
    }

    public void createEaterNode(GridCoord coord, int fraction) {
        NodeSpecification spec = new EaterSpecification(fraction);
        createNode(coord, spec);
    }

    public void createSplitterNode(GridCoord coord) {
        NodeSpecification spec = new SplitterSpecification();
        createNode(coord, spec);
    }

    /**
     * Finds a node between to positions and bind a connection between them.
     *
     * @param startPos Start startPosition
     * @param endPos End position
     * @param conn The connection
     */
    public void bind(GridCoord startPos, GridCoord endPos, Connection conn) {
        Node n1 = nodes.get(startPos);
        Node n2 = nodes.get(endPos);
        
        if(n1 == null || n2 == null){
            return; //Couldn't finde nodes.
        }
        
        conn.setStartPos(n1.getPosition());
        conn.setEndPos(n2.getPosition());
        n1.getNodeSpecification().addOutgoingConnection(conn);
        n2.getNodeSpecification().addIncomingConnection(conn);
    }

    public Collection<Node> getNodeList() {
        return nodes.values();
    }
    
    public Node getNode(GridCoord coord){
        return nodes.get(coord);
    }
    
    public boolean hasNode(GridCoord coord){
        return nodes.containsKey(coord);
    }
}