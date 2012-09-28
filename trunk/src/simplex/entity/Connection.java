package simplex.entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import simplex.util.ImageManager;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class Connection implements Entity {

    private Queue<ResourceBall> movingResources = new LinkedList<>();
    private Queue<ResourceBall> waitingResources = new LinkedList<>();
    private Node endNode;
    private Node startNode;
    private Resource resource;
    
    
    public Connection() {}

    public Node getStartNode() {
        return startNode;
    }
    
    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    @Override
    public void render(Graphics g) {
        Vector2f startPos = startNode.getPosition();
        Vector2f endPos = endNode.getPosition();

        g.setColor(Color.cyan);
        g.drawLine(startPos.x + 17, startPos.y + 17, endPos.x + 17,
                endPos.y + 17);
        for (ResourceBall resourceBall : movingResources) {
            resourceBall.render(g);
        }
        
    }

    @Override
    public void update(int delta) {
        Vector2f startPos = startNode.getPosition();
        Vector2f endPos = endNode.getPosition();

        final Resource r = startNode.getNodeSpecification().getResource();
        if (r != null && !r.equals(resource)) {
            this.resource = r;
            float speed = ((startPos.distanceSquared(endPos)) / r.getRate());
            while (waitingResources.size() + movingResources.size() < r.getRate()) {
                waitingResources.add(new ResourceBall(startPos.copy(), speed));
            }
        }

        float k = 0.05f;
        Vector2f dir = endPos.copy().sub(startPos).normalise().scale(k);

        // Move another waiting resource to the movingResource queue
        // if there is waiting resources and if the first moving resource has
        // moved far enough.
        if (!waitingResources.isEmpty()
                && (movingResources.isEmpty() || isTimeForNextBall())) {
            movingResources.add(waitingResources.poll());
        }

        Iterator<ResourceBall> it = movingResources.iterator();
        while (it.hasNext()) {

            ResourceBall resourceBall = it.next();
            resourceBall.position.add(dir);

            resourceBall.setResource(resource.getColorType());

            // Reset the moving resource when it has moved from start to end
            if (hasReachedEnd(resourceBall)) {
                endNode.getNodeSpecification().setResource(resource, this);
                resourceBall.position = startPos.copy();
                waitingResources.add(resourceBall);
                it.remove();
            }
        }
    }

    private boolean hasReachedEnd(ResourceBall ball) {
        Vector2f endPos = endNode.getPosition();
        final float nearConstant = 1;
        float dist = ball.position.distanceSquared(endPos);
        return dist < nearConstant;
    }

    private boolean isTimeForNextBall() {
        boolean hasReachedBallLimit = movingResources.size() >= resource.getRate();

        Vector2f startPos = startNode.getPosition();
        Vector2f endPos = endNode.getPosition();

        // Find the distance of the ball which have traveled shortest
        float previousBallDist = startPos.distance(endPos);
        for (ResourceBall resourceBall : movingResources) {
            float ballDist = resourceBall.position.distance(startPos);
            if (ballDist < previousBallDist) {
                previousBallDist = ballDist;
            }
        }

        float nextBallThreshold = startPos.distance(endPos) / resource.getRate();

        return !hasReachedBallLimit && previousBallDist > nextBallThreshold;
    }
    
    public void swapDirection(){
        Node temp = startNode;
        startNode = endNode;
        endNode = temp;                
    }
}

class ResourceBall {

    Vector2f position;
    Image img = ImageManager.white_resource;
    float speed;

    public ResourceBall(Vector2f position, float speed) {
        this.position = position;
        this.speed = speed;
    }

    void render(Graphics g) {
        g.drawImage(img, position.x, position.y);
    }

    void setResource(Color resourceType) {
        if (resourceType.equals(Color.red)) {
            img = ImageManager.red_resource;
        } else if (resourceType.equals(Color.green)) {
            img = ImageManager.green_resource;
        } else if (resourceType.equals(Color.blue)) {
            img = ImageManager.blue_resource;
        }
    }
}

