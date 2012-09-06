/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Emil
 */
public class Node extends AbstractComponent {

    private int x, y, width, height;
    private List<Connection> connections = new LinkedList<>();

    public Node(GUIContext container) {
        super(container);
    }

    @Override
    public void render(GUIContext guic, Graphics g) throws SlickException {
        //Render the node;
        
        
        
        for (Connection conn : connections) {
            conn.render(guic, g);
        }
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void removeConnection(Connection conn) {
        connections.remove(conn);
    }

    public void addConnection(Connection conn) {
        connections.add(conn);
    }
}
