package simplex.level.saved;

import simplex.entity.Connection;
import simplex.entity.Node;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedConnection extends SavedEntity {
    private int startNodeId;
    private int endNodeId;

    public SavedConnection() {
    }

    public SavedConnection(Connection conn) {
        super(conn);
        this.startNodeId = conn.getStartNode().getId();
        this.endNodeId = conn.getEndNode().getId();
    }

    public int getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(int startNodeId) {
        this.startNodeId = startNodeId;
    }

    public int getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(int endNodeId) {
        this.endNodeId = endNodeId;
    }
    
    @Override
    public Connection create(){
        Connection conn = new Connection();
        Node n1 = SavedNodeFactory.instance().getNode(startNodeId);
        Node n2 = SavedNodeFactory.instance().getNode(endNodeId);
        conn.bind(n1, n2);
        return conn;
    }
    
}
