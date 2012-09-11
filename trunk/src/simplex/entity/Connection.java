/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Emil
 */
public class Connection {

    private Node node;
    private Vector2f startPos;
    private Vector2f endPos;
    private int rate = 1;
    private int resourceType;
    private ResourceBall resourceBall;
    
    

    public Connection(Node node) {
	this.node = node;
	endPos = node.getPosition();
	resourceBall = new ResourceBall();
    }

    public void render(Graphics g) {
	g.setColor(Color.yellow);
	g.drawLine(startPos.x, startPos.y, endPos.x, endPos.y);
	resourceBall.render(g);
	node.render(g);
    }

    void setResourceType(int resourceType) {
	this.resourceType = resourceType;
    }

    void setResourceRate(int rate) {
	this.rate = rate;
    }

    void update(int delta) {
	//TODO: Move the resource balls.

	float k = 0.1f;
	
	Vector2f dir = endPos.copy().sub(startPos).normalise().scale(rate*k);
	resourceBall.position.add(dir);
	
	
	float dist = resourceBall.position.distanceSquared(endPos);
	if(dist < 1 ){
	    resourceBall.position = startPos.copy();
	}
	
	node.update(delta);
    }

    /**
     * Set the position where the connection starts.
     * @param startPosition the startPosition to set
     */
    public void setStartPosition(Vector2f startPosition) {
	this.startPos = startPosition;
	resourceBall.position.set(startPosition.copy());
    }
}

class ResourceBall{

    Vector2f position = new Vector2f();

    
    void render(Graphics g) {
	g.setColor(Color.pink);
	g.fillOval(position.x, position.y, 10, 10);
    }

}