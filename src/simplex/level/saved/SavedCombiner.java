package simplex.level.saved;

import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.specification.CombinerSpecification;
import simplex.entity.specification.NodeSpecification;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedCombiner extends SavedNode {
    private int fraction;

    private SavedCombiner() {
    }

    public SavedCombiner(Node node) {
        super(node);
        NodeSpecification spec = node.getNodeSpecification();
        CombinerSpecification combinerSpecification = (CombinerSpecification) spec;
    }

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createCombinerNode();
        node.setGridPosition(getCoord());
        CombinerSpecification spec = (CombinerSpecification) node.getNodeSpecification();
        return node;
    }    
}
