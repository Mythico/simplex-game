package simplex.level.saved;

import simplex.entity.Node;
import simplex.entity.NodeFactory;

/**
 * A Saved Dummy used for serializing and de serializing a node with
 * DummySpecification as its node specification.
 
 * @author Emil
 * @author Samuel
 */
public class SavedDummy extends SavedNode {

    @SuppressWarnings("unused")
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
