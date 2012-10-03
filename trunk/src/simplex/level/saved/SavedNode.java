package simplex.level.saved;

import simplex.entity.Node;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public abstract class SavedNode extends SavedEntity  {

    private GridCoord coord;

    public SavedNode() {
    }

    public SavedNode(GridCoord coord, int id) {
        super(id);
        this.coord = coord;
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
