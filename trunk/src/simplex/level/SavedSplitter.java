package simplex.level;

import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedSplitter extends SavedNode {

    private SavedSplitter() {
    }

    public SavedSplitter(GridCoord coord, int id) {
        super(coord, id);
    }

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createSplitterNode();
        node.setGridPosition(getCoord());
        return node;
    }    
}
