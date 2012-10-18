package simplex.level.saved;

import simplex.entity.Node;
import simplex.entity.NodeFactory;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedCombiner extends SavedNode {

    @SuppressWarnings("unused")
    private SavedCombiner() {
    }

    public SavedCombiner(Node node) {
        super(node);
    }

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createCombinerNode();
        node.setGridPosition(getCoord());        
        return node;
    }    
}
