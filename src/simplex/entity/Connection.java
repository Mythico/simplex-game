/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Emil
 */
public class Connection extends AbstractComponent {

    private Node startNode;
    private Node endNode;
    private boolean fromStartToEnd;
    private int x, y, width, height;

    public Connection(GUIContext container, Node startNode, Node endNode) {
        super(container);
        this.startNode = startNode;
        this.endNode = endNode;
        startNode.addConnection(this);
        endNode.addConnection(this);

    }

    /**
     * Removes the connection between this object and the connected nodes.
     */
    public void remove() {
        startNode.removeConnection(this);
        endNode.removeConnection(this);
    }

    @Override
    public void render(GUIContext guic, Graphics g) throws SlickException {
        x = startNode.getX();
        y = startNode.getY();
        width = endNode.getX();
        height = endNode.getY();


        
        //Draw line
        g.setColor(Color.yellow);
        g.drawLine(x, y, width, height);
        

    }

    public void toggleDirection() {
        fromStartToEnd = !fromStartToEnd;
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
}
