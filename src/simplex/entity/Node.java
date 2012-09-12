/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * 
 * @author Emil
 * @author Samuel
 */
public class Node {

    private Vector2f position;
    private int type;
    private List<Connection> connections = new LinkedList<>();

    private int resourceType;
    private int rate = 1;
    private Image img;

    public Node(Vector2f pos) {
        position = pos;
    }

    public void render(Graphics g) {

        switch (resourceType) {
        case 0:
            g.setColor(Color.red);
            break;
        case 1:
            g.setColor(Color.green);
            break;
        case 2:
            g.setColor(Color.blue);
            break;
        }

        g.drawImage(img, position.x, position.y);
        for (Connection conn : connections) {
            conn.render(g);
        }
    }

    public void update(int delta) {

        for (Connection conn : connections) {
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

    public List<Connection> getConnections() {
        return connections;
    }

    public boolean isFactory() {
        return type == 0;
    }

    public boolean isConsumer() {
        return type == 1;
    }

    public boolean isPassive() {
        return type == 2;
    }

    public boolean isEater() {
        return type == 3;
    }

    public boolean isSpectrum() {
        return type == 4;
    }

    void setImage(Image img) {
        this.img = img;
    }
}
