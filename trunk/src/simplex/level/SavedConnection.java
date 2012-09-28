package simplex.level;

import simplex.entity.Connection;
import simplex.entity.Node;
import simplex.entity.NodeFactory;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedConnection {

    private int id;
    private int startNodeId;
    private int endNodeId;

    public SavedConnection() {
    }

    SavedConnection(int startNodeId, int endNodeId, int id) {
        this.id = id;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    

    public Connection create(){
        Connection conn = new Connection();
        Node n1 = SavedNodeFactory.instance().getNode(startNodeId);
        Node n2 = SavedNodeFactory.instance().getNode(endNodeId);
        NodeFactory.instance().bind(n1, n2, conn);
        return conn;
    }
    
}
