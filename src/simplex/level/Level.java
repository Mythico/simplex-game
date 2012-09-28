package simplex.level;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import simplex.entity.Connection;
import simplex.entity.Node;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class Level {

    private Map<GridCoord, Node> nodes = new HashMap<>();
    private List<Connection> connections = new LinkedList<>();

    public Map<GridCoord, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<GridCoord, Node> nodes) {
        this.nodes = nodes;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    
}
