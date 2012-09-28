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

    public SavedSplitter(int x, int y, int id) {
        super(x, y, id);
    }

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createSplitterNode();
        node.setGridPosition(new GridCoord(getX(), getY()));
        return node;
    }    
}
