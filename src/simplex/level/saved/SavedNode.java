package simplex.level.saved;

import simplex.entity.Node;
import simplex.util.GridConversions;
import simplex.util.GridCoord;

/**
 * An abstract class for all the nodes that will be serialized to YAML objects.
 *
 * @author Emil
 * @author Samuel
 */
public abstract class SavedNode extends SavedEntity  {

    private GridCoord coord;

    /**
     * An empty constructor for the YAML serialization.
     */
    public SavedNode() {
    }

    /**
     * Creates a new SavedNode with coordinates and an id.
     * @param node The node that will be saved.
     */
    public SavedNode(Node node) {
        super(node);
        this.coord = GridConversions.screenToGrid(node.getPosition());
    }

    /**
     * Get the coordinates.
     * @return The coordinates.
     */
    public GridCoord getCoord() {
        return coord;
    }

    /**
     * Set the coordinates.
     * @param coord The coordinates to be set.
     */
    public void setCoord(GridCoord coord) {
        this.coord = coord;
    }
    
    /**
     * Create a node from a SavedNode.
     * @return A new Node.
     */
    @Override
    public abstract Node create();
    
}
