package simplex.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import simplex.entity.specification.NodeSpecification;
import simplex.util.GridConversions;
import simplex.util.GridCoord;
import simplex.util.ImageManager;

/**
 * A class for the nodes.
 * 
 * @author Emil
 * @author Samuel
 */
public class Node implements Entity{

    private float x, y;

    private NodeSpecification nodeSpecification;
    private boolean selected;
    private final int id;
    
    public Node() {
        this(null);
    }

    Node(NodeSpecification spec) {        
        nodeSpecification = spec;        
        id = NodeFactory.instance().getNewId();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void render(Graphics g) {
        Image img = ImageManager.get(nodeSpecification);
        g.drawImage(img, x, y);
        if(selected){
            g.setColor(Color.green);
            g.drawRect(x, y, GridConversions.getGridWidth(), GridConversions.getGridHeight());
        }
    }

    @Override
    public void update(int delta) {
    }

    /**
     * Gets the specification for this node.
     * @return the specification
     */
    public NodeSpecification getNodeSpecification() {
        return nodeSpecification;
    }

    /**
     * Sets the specification for this node.
     * @param nodeSpecification the specification
     */
    public void setNodeSpecification(NodeSpecification nodeSpecification) {
        this.nodeSpecification = nodeSpecification;
    }     

    /**
     * Gets the position vector for this node.
     * @return the position vector
     */
    public Vector2f getPosition() {
        return new Vector2f(x,y);
    }    
   
    /**
     * Sets the position in the grid for this node.
     * @param coord the grid coordinates
     */
    public void setGridPosition(GridCoord coord){
        Vector2f pos = GridConversions.gridToScreenCoord(coord);
        this.x = pos.x;
        this.y = pos.y;
    }
    
    /**
     * Gets the X coordinates for this node.
     * @return the X coordinates
     */
    public float getX() {
        return x;
    }
    
    /**
     * Sets the X coordinates for this node.
     * @param x the X coordinates
     */
    public void setX(float x) {
        this.x = x;
    }
    
    /**
     * Gets the Y coordinates for this node.
     * @return the Y coordinates
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the Y coordinates for this node.
     * @param y the Y coordinates
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Check if this node is selected.
     * @return if it is selected or not
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets whether this node is selected or not.
     * @param b if it should be selected or not
     */
    public void setSelected(boolean b) {
        selected = b;
    }
    
}
