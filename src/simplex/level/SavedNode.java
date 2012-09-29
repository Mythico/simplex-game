package simplex.level;

import simplex.entity.Node;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public abstract class SavedNode {

    private int id;
    private GridCoord coord;

    public SavedNode() {
    }

    SavedNode(GridCoord coord, int id) {
        this.id = id;
        this.coord = coord;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GridCoord getCoord() {
        return coord;
    }

    public void setCoord(GridCoord coord) {
        this.coord = coord;
    }

    

    public abstract Node create();
    
}
