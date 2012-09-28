package simplex.level;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Graphics;
import simplex.entity.Connection;
import simplex.entity.Entity;
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

    public void render(Graphics g) {        
        if (nodes != null) {
            for (Node node : nodes.values()) {
                node.render(g);
            }
        }
        
        if (connections != null) {
            for (Entity conn : connections) {
                conn.render(g);
            }
        }
    }

    public void update(int delta) {
        if (nodes != null) {
            for (Node node : nodes.values()) {
                node.update(delta);
            }
        }
        if (connections != null) {
            for (Entity conn : connections) {
                conn.update(delta);
            }
        }
    }

    
}
