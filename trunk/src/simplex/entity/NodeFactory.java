/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Emil
 */
public class NodeFactory {
    
    private final GameContainer gc;
    private final float width;
    private final float height;

    public NodeFactory(GameContainer gc, int width, int height) {
	this.gc = gc;
	this.width = width;
	this.height = height;
    }
    
    public Node createFactory(int x, int y, int resourceType, int resourceRate){
	Node n = new Node(convertGridCoordToRealCoord(x, y));
	n.setResourceRate(resourceRate);
	n.setResourceType(resourceType);	
	return n;
    }
    
    public Node createDummyNode(int x, int y){
	return new Node(convertGridCoordToRealCoord(x, y));
    }
    
    private Vector2f convertGridCoordToRealCoord(int x, int y){
	float fx = x * (gc.getWidth() / width);
	float fy = y * (gc.getHeight() / height);
	return new Vector2f(fx, fy);
    }
}
