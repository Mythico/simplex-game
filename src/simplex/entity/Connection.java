/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import simplex.util.ImageManager;

/**
 * 
 * @author Emil, Samuel
 */
public class Connection {

    private Node node;
    private Vector2f startPos;
    private Vector2f endPos;
    private int rate = 1;
    private int resourceType;
    private List<ResourceBall> resourceBalls = new LinkedList<>();

    public Connection(Node node) {
        this.node = node;
        endPos = node.getPosition();
        //while (resourceBalls.size() < rate) {
        resourceBalls.add(new ResourceBall());
        resourceBalls.add(new ResourceBall());
        //}
        System.out.println("balls: " + resourceBalls.size());
    }

    public void render(Graphics g) {
        for (ResourceBall resourceBall : resourceBalls) { 
            resourceBall.render(g);
        }
    }

    void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    void setResourceRate(int rate) {
        this.rate = rate;
    }

    int getResourceType() {
        return resourceType;
    }

    void update(int delta) {

        Queue<ResourceBall> movingResources = new LinkedList<>();
        Queue<ResourceBall> waitingResources = new LinkedList<>();
        
        waitingResources.addAll(resourceBalls);
        
        float k = 0.05f;
        Vector2f dir = endPos.copy().sub(startPos).normalise().scale(k);
                
        // Move another waiting resource to the movingResource queue 
        // if there is waiting resources and if the first moving resource has moved far enough.
        if (movingResources.isEmpty() || (!waitingResources.isEmpty() && (movingResources.size() < rate) &&
                (movingResources.peek().position.distance(startPos) > startPos.distance(endPos)/rate))) {
            movingResources.add(waitingResources.poll());
        }
        
        Iterator<ResourceBall> it = movingResources.iterator();
        while (it.hasNext()) {
            
            ResourceBall resourceBall = it.next();
            resourceBall.position.add(dir);
    
            switch (resourceType) {
            case 0: resourceBall.img = ImageManager.red_resource;
                break;
            case 1: resourceBall.img = ImageManager.green_resource;
                break;
            case 2: resourceBall.img = ImageManager.blue_resource;
                break;
            }
    
            // Reset the moving resource when it has moved from start to end
            float dist = resourceBall.position.distanceSquared(endPos);
            if (dist < 1) {
                resourceBall.position = startPos.copy();
                waitingResources.add(resourceBall);
                it.remove();
            }
        }

        node.update(delta);
    }

    /**
     * Set the position where the connection starts.
     * 
     * @param startPosition
     *            the startPosition to set
     */
    public void setStartPosition(Vector2f startPosition) {
        this.startPos = startPosition;

        for (ResourceBall resourceBall : resourceBalls) { 
            resourceBall.position.set(startPosition.copy());
        }
    }

}

class ResourceBall {
    Vector2f position = new Vector2f();

    Image img = ImageManager.white_resource;

    void render(Graphics g) {
        g.drawImage(img, position.x, position.y);
    }
}
