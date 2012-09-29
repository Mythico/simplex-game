package simplex.entity;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
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

    private Deque<ResourceBall> movingResources = new LinkedList<>();
    private Node endNode;
    private Node startNode;

    public Connection() {
    }

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
        

        // Move another waiting resource to the movingResource queue
        // if there is waiting resources and if the first moving resource has
        // moved far enough.
        if (r.getRate() > 0 && (movingResources.isEmpty() || isTimeForNextBall())) {
            movingResources.add(new ResourceBall(startPos, endPos, r));
        }

        Iterator<ResourceBall> it = movingResources.iterator();
        while (it.hasNext()) {

            ResourceBall resourceBall = it.next();
            resourceBall.update(delta);

            // Reset the moving resource when it has moved from start to end
            if (resourceBall.hasReachedEnd()) {
                final NodeSpecification spec = endNode.getNodeSpecification();
                spec.setResource(resourceBall.getResource());
                it.remove();
            }
        }
    }

    private boolean isTimeForNextBall() {

        Vector2f startPos = startNode.getPosition();
        Vector2f endPos = endNode.getPosition();

        float halfDistance = startPos.distanceSquared(endPos) / 2;
        final Vector2f ballPos = movingResources.peekLast().getPosition();        
        float ballDistance = startPos.distanceSquared(ballPos);
        
        return halfDistance < ballDistance;
    }

    public void swapDirection() {
        Node temp = startNode;
        startNode = endNode;
        endNode = temp;
    }
}

class ResourceBall {

    private final Vector2f position;
    private final Resource resource;
    private final Vector2f endPos;
    private final Vector2f dir;

    public ResourceBall(Vector2f startPos, Vector2f endPos, Resource r) {
        this.position = startPos.copy();
        this.endPos = endPos;
        this.resource = r;

        float k = 0.05f * (float)r.getRate();
        dir = endPos.copy().sub(startPos).normalise().scale(k);
    }

    void render(Graphics g) {
        Image img = ImageManager.get(resource);
        g.drawImage(img, position.x, position.y);
    }

    void update(int delta) {
        position.add(dir);
    }

    Resource getResource() {
        return resource;
    }
    
    boolean hasReachedEnd() {
        final float nearConstant = 1;
        float dist = position.distanceSquared(endPos);
        return dist < nearConstant;
    }    

    Vector2f getPosition() {
        return position;
    }
}
