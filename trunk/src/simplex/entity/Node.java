package simplex.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import simplex.util.GridConversions;
import simplex.util.GridCoord;
import simplex.util.ImageManager;

/**
 * 
 * @author Emil
 * @author Samuel
 */
public class Node implements Entity{

    private float x, y;;

    private NodeSpecification nodeSpecification;
    private boolean selected;
    
    public Node() {}

    Node(NodeSpecification spec) {
        nodeSpecification = spec;        
    }

    @Override
    public void render(Graphics g) {
        Image img = ImageManager.get(nodeSpecification);
        g.drawImage(img, x, y);
        if(selected){
            g.setColor(Color.green);
            g.drawRect(x, y, GridConversions.getGridWidth(), GridConversions.getGridWidth());
        }
    }

    @Override
    public void update(int delta) {
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
   
    public void setGridPosition(GridCoord coord){
        Vector2f pos = GridConversions.gridToScreenCoord(coord);
        this.x = pos.x;
        this.y = pos.y;
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean b) {
        selected = b;
    }
    
}
