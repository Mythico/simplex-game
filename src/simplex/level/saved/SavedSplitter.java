package simplex.level.saved;

import simplex.entity.Node;
import simplex.entity.NodeFactory;

/**
 * A Saved Splitter used for serializing and de serializing a node with
 * SplitterSpecification as its node specification.
 *
 * @author Emil
 * @author Samuel
 */
public class SavedSplitter extends SavedNode {

    @SuppressWarnings("unused")
    private SavedSplitter() {
    }

    public SavedSplitter(Node node) {
        super(node);
    }

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createSplitterNode();
        node.setGridPosition(getCoord());
        return node;
    }    
}
