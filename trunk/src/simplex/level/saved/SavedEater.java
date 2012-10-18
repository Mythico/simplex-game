package simplex.level.saved;

import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.NodeSpecification;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedEater extends SavedNode {
    private int fraction;

    @SuppressWarnings("unused")
    private SavedEater() {
    }

    public SavedEater(Node node) {
        super(node);
        NodeSpecification spec = node.getNodeSpecification();
        EaterSpecification eaterSpecification = (EaterSpecification) spec;
        this.fraction = eaterSpecification.getFraction();
    }

    public int getFraction() {
        return fraction;
    }

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }   

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createEaterNode();
        node.setGridPosition(getCoord());
        EaterSpecification spec = (EaterSpecification) node.getNodeSpecification();
        spec.setFraction(fraction);
        return node;
    }    
}
