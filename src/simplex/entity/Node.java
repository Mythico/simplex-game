/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Emil
 */
public class Node {

    private Vector2f position;
    private List<Connection> connections = new LinkedList<>();
    
    private int resourceType;
    private int rate = 1;

    public Node(Vector2f pos) {
	position = pos;
    }

    public void render(Graphics g) {
	g.setColor(Color.red);
	g.fillOval(position.x, position.y, 20, 20);
	for(Connection conn : connections){
	    conn.render(g);
	}
    }
    
    public void update(int delta) {
	
	for(Connection conn : connections){
	    conn.setResourceType(resourceType);
	    conn.setResourceRate(rate);	    
	    conn.update(delta);
	}
    }

    public void removeConnection(Connection conn) {
        connections.remove(conn);
    }

    public void addConnection(Connection conn) {
        connections.add(conn);
	conn.setStartPosition(position);
    }

    public Vector2f getPosition() {
	return position;
    }

    void setResourceRate(int resourceRate) {
	rate = resourceRate;
    }

    void setResourceType(int resourceType) {
	this.resourceType = resourceType;
    }
}
