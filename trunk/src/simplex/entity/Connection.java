package simplex.entity;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import simplex.entity.specification.FactorySpecification;
import simplex.entity.specification.NodeSpecification;
import simplex.entity.specification.Observer;
import simplex.util.ImageManager;

/**
 * A class for the connection between nodes.
 *
 * @author Emil
 * @author Samuel
 */
public class Connection implements Entity, Observer {

    private final Deque<ResourceBall> movingResources = new LinkedList<>();
    private Node endNode;
    private Node startNode;
    private final int id;

    public Connection() {
        id = NodeFactory.instance().getNewId();
    }

    @Override
    public int getId() {
        return id;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        if (this.startNode != null) {
            this.startNode.getNodeSpecification().deleteObserver(this);
        }
        this.startNode = startNode;
        startNode.getNodeSpecification().addObserver(this);
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    /**
     * Binds two nodes to the connection.     *
     * @param startNode Start node
     * @param endNode End node
     */
    public void bind(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    @Override
    public void render(Graphics g) {
        Vector2f startPos = startNode.getPosition();
        Vector2f endPos = endNode.getPosition();

        // Draw a line between the startNode and the endNode
        g.setColor(Color.cyan);
        g.drawLine(startPos.x + 17, startPos.y + 17, endPos.x + 17,
                endPos.y + 17);
        synchronized (movingResources) {
            for (ResourceBall resourceBall : movingResources) {
                resourceBall.render(g);
            }
        }

    }

    @Override
    public void update(int delta) {
        synchronized (movingResources) {
            Iterator<ResourceBall> it = movingResources.iterator();
            while (it.hasNext()) {

                ResourceBall resourceBall = it.next();
                resourceBall.update(delta);

                // Reset the moving resource when it has moved from start to end
                if (resourceBall.hasReachedEnd()) {
                    final NodeSpecification spec = endNode.getNodeSpecification();
                    if(!(spec instanceof FactorySpecification)) {
                        spec.setResource(resourceBall.getResource());
                    }
                    it.remove();
                }
            }
        }
    }

    /**
     * Swap the direction of the resources on this connection.
     */
    public void swapDirection() {
        movingResources.clear();
        Node temp = startNode;
        setStartNode(endNode);
        setEndNode(temp);
    }

    /**
     * Check if this connection is connected to the node.
     *
     * @param node The node to be checked against.
     * @return True if this connection is connected, otherwise false.
     */
    public boolean isConnectedTo(Node node) {
        return endNode.equals(node) || startNode.equals(node);
    }

    @Override
    public void update(Resource r) {
        if (r == null) {
            return;
        }
        Vector2f startPos = startNode.getPosition();
        Vector2f endPos = endNode.getPosition();

        // Move another waiting resource to the movingResource queue
        // if there is waiting resources and if the first moving resource has
        // moved far enough.
        synchronized (movingResources) {
            if (r.getRate() > 0 && (movingResources.isEmpty())) {
                movingResources.add(new ResourceBall(startPos, endPos, r));
            }
        }
    }
}

/**
 * A class for the graphical representation of the resources.
 * 
 * @author Samuel
 * @author Emil
 *
 */
class ResourceBall {

    private final Vector2f position;
    private final Resource resource;
    private final Vector2f endPos;
    private final Vector2f dir;
    private Image img;

    public ResourceBall(Vector2f startPos, Vector2f endPos, Resource r) {
        this.position = startPos.copy();
        this.endPos = endPos;
        this.resource = r;

        float k = 0.05f;
        img = ImageManager.get(resource);
        dir = endPos.copy().sub(startPos).normalise().scale(k);
    }

    void render(Graphics g) {
        img.drawCentered(position.x, position.y);
    }

    void update(int delta) {
        position.add(dir.copy().scale(delta));
        img = ImageManager.get(resource);
    }

    Resource getResource() {
        return resource;
    }

    /**
     * Check whether this resource has reached the end of its path.
     * @return if the resource has reached the end.
     */
    boolean hasReachedEnd() {
        final float nearConstant = 1;
        float dist = position.distanceSquared(endPos);
        return dist < nearConstant;
    }

    Vector2f getPosition() {
        return position;
    }
}
