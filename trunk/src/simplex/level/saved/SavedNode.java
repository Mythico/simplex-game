package simplex.level.saved;

import simplex.entity.Node;
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
     * @param coord The coordinates node that is being saved.
     * @param id  The id of the node that is being saved.
     */
    public SavedNode(GridCoord coord, int id) {
        super(id);
        this.coord = coord;
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
