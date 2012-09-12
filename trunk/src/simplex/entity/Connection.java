/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import simplex.util.ImageManager;

/**
 * 
 * @author Emil
 * @author Samuel
 */
public class Connection {

    private Node node;
    private Vector2f startPos;
    private Vector2f endPos;
    private int rate = 1;
    private int resourceType;
    private Queue<ResourceBall> movingResources = new LinkedList<>();
    private Queue<ResourceBall> waitingResources = new LinkedList<>();

    public Connection(Node node) {
        this.node = node;
        endPos = node.getPosition();
    }

    public void render(Graphics g) {
        for (ResourceBall resourceBall : movingResources) {
            resourceBall.render(g);
        }
    }

    void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    void setResourceRate(int rate) {
        this.rate = rate;
        while (waitingResources.size() + movingResources.size() < rate) {
            waitingResources.add(new ResourceBall(startPos.copy()));
        }
    }

    int getResourceType() {
        return resourceType;
    }

    void update(int delta) {

        float k = 0.05f;
        Vector2f dir = endPos.copy().sub(startPos).normalise().scale(k);

        // Move another waiting resource to the movingResource queue
        // if there is waiting resources and if the first moving resource has
        // moved far enough.
        if (movingResources.isEmpty() || isTimeForNextBall()) {
            movingResources.add(waitingResources.poll());
        }

        Iterator<ResourceBall> it = movingResources.iterator();
        while (it.hasNext()) {

            ResourceBall resourceBall = it.next();
            resourceBall.position.add(dir);

            resourceBall.setResource(resourceType);

            // Reset the moving resource when it has moved from start to end
            if (hasReachedEnd(resourceBall)) {
                resourceBall.position = startPos.copy();
                waitingResources.add(resourceBall);
                it.remove();
            }
        }

        node.update(delta);
    }

    private boolean hasReachedEnd(ResourceBall ball) {
        final float nearConstant = 1;
        float dist = ball.position.distanceSquared(endPos);
        return dist < nearConstant;
    }

    private boolean isTimeForNextBall() {
        boolean hasReachedBallLimit = movingResources.size() >= rate;

        // Find the distance of the ball which have traveled shortest
        float previousBallDist = startPos.distance(endPos);
        for (ResourceBall resourceBall : movingResources) {
            float ballDist = resourceBall.position.distance(startPos);
            if (ballDist < previousBallDist) {
                previousBallDist = ballDist;
            }
        }

        float nextBallThreshold = startPos.distance(endPos) / rate;

        return !waitingResources.isEmpty() && !hasReachedBallLimit
                && previousBallDist > nextBallThreshold;
    }

    /**
     * Set the position where the connection starts.
     * 
     * @param startPosition
     *            the startPosition to set
     */
    public void setStartPosition(Vector2f startPosition) {
        this.startPos = startPosition;

        waitingResources.clear();
        while (waitingResources.size() + movingResources.size() < rate) {
            waitingResources.add(new ResourceBall(startPosition.copy()));
        }
    }
}

class ResourceBall {

    Vector2f position;
    Image img = ImageManager.white_resource;

    public ResourceBall(Vector2f position) {
        this.position = position;
    }

    void render(Graphics g) {
        g.drawImage(img, position.x, position.y);
    }

    void setResource(int resourceType) {
        switch (resourceType) {
        case 0:
            img = ImageManager.red_resource;
            break;
        case 1:
            img = ImageManager.green_resource;
            break;
        case 2:
            img = ImageManager.blue_resource;
            break;
        }
    }
}
