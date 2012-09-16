package simplex.entity;

import org.newdawn.slick.Color;
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

    private float x, y;;
    private NodeSpecification nodeSpecification;
    private boolean selected;

    public Node(Vector2f pos, NodeSpecification specification) {
        x = pos.x;
        y = pos.y;
        nodeSpecification = specification;
    }

    Node(NodeSpecification spec) {
        nodeSpecification = spec;        
    }

    @Override
    public void render(Graphics g) {
        Image img = nodeSpecification.getImage();
        g.drawImage(img, x, y);
        if(selected){
            g.setColor(Color.green);
            g.drawRect(x, y, GridConversions.getGridWidth(), GridConversions.getGridWidth());
        }
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
        return new Vector2f(x,y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        
    }

    public void setPosition(Vector2f pos) {
        x = pos.x;
        y = pos.y;
    }

    public void setSelected(boolean b) {
        selected = b;
    }

    
}
