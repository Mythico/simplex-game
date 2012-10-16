package simplex.level.saved;

import simplex.entity.Node;
import simplex.entity.NodeFactory;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedDummy extends SavedNode {

    private SavedDummy() {
    }

    public SavedDummy(Node node) {
        super(node);
    }

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createDummyNode();
        node.setGridPosition(getCoord());
        return node;
    }
}
