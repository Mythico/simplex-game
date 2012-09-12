/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

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
    private ResourceBall resourceBall;

    public Connection(Node node) {
        this.node = node;
        endPos = node.getPosition();
        resourceBall = new ResourceBall();
    }

    public void render(Graphics g) {
	resourceBall.render(g);
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
        // TODO: Move the resource balls.

        float k = 0.05f;

        Vector2f dir = endPos.copy().sub(startPos).normalise().scale(rate * k);
        resourceBall.position.add(dir);

        switch (resourceType) {
        case 0:
            resourceBall.img = ImageManager.red_resource;
            break;
        case 1:
            resourceBall.img = ImageManager.green_resource;
            break;
        case 2:
            resourceBall.img = ImageManager.blue_resource;
            break;
        }

        float dist = resourceBall.position.distanceSquared(endPos);
        if (dist < 1) {
            resourceBall.position = startPos.copy();
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
        resourceBall.position.set(startPosition.copy());
    }

}

class ResourceBall {
    Vector2f position = new Vector2f();

    Image img = ImageManager.white_resource;

    void render(Graphics g) {
        g.drawImage(img, position.x, position.y);
    }
}
