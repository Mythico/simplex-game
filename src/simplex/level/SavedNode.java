package simplex.level;

import simplex.entity.Node;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public abstract class SavedNode extends SavedEntity  {

    private int id;
    private GridCoord coord;

    public SavedNode() {
    }

    SavedNode(GridCoord coord, int id) {
        super(id);
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

    

    @Override
    public abstract Node create();
    
}
