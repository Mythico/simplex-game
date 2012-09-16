/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import simplex.util.GridConversions;

/**
 * 
 * @author Emil
 * @author Samuel
 */
public class Node implements Entity{

    private Vector2f position;
    private NodeSpecification nodeSpecification;

    public Node(Vector2f pos, NodeSpecification specification) {
        position = pos;
        nodeSpecification = specification;
    }

    @Override
    public void render(Graphics g) {
        Image img = nodeSpecification.getImage();
        g.drawImage(img, position.x, position.y);
    }

    @Override
    public void update(int delta) {
        nodeSpecification.update(delta);
    }

    public NodeSpecification getNodeSpecification() {
        return nodeSpecification;
    }

    public void setNodeSpecification(NodeSpecification nodeSpecification) {
        this.nodeSpecification = nodeSpecification;
    }    

    public Vector2f getPosition() {
        return position;
    }
}
