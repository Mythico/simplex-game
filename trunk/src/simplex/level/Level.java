package simplex.level;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Graphics;
import simplex.entity.Connection;
import simplex.entity.Entity;
import simplex.entity.Node;
import simplex.util.GridConversions;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class Level {
    
    private Map<GridCoord, Node> nodes = new HashMap<>();
    private List<Connection> connections = new LinkedList<>();
    private final String name;

    public Level(String name) {
        this.name = name;
    }

    public Level() {
        name = "";
    }
    
    /**
     * Get the name of the level.
     * @return The name of the level.
     */
    public String getName(){
        return name;
    }

    /**
     * Get the nodes on this level.
     * @return Nodes on this level
     */
    public Map<GridCoord, Node> getNodes() {
        return nodes;
    }

    /**
     * Set the nodes on this level.
     * @param nodes The nodes to be set.
     */
    public void setNodes(Map<GridCoord, Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * Get the connection on this level.
     * @return The connection of the level.
     */
    public List<Connection> getConnections() {
        return connections;
    }

    /**
     * Set the connection on this level.
     * @param connections The connection to be set.
     */
    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    /**
     * Render the level.
     * @param g The graphics the level will be rendered with.
     */
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

    /**
     * Update the level
     * @param delta Time from last update.
     */
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
    
    
    /**
     * Clears all dependencies for the node and then removes the node
     * from the level.
     * @param node The node to be removed.
     */
    public void removeNode(Node node) {
        //Remove all the connection connected to the picked node.
        Iterator<Connection> it = connections.iterator();
        while (it.hasNext()) {
            Connection conn = it.next();
            if (conn.isConnectedTo(node)) {
                it.remove();
            }
        }
        GridCoord coord = GridConversions.screenToGrid(node.getPosition());
        nodes.remove(coord);
    }

    
}
