package simplex.level.saved;

import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.NodeSpecification;

/**
 * A Saved Eater used for serializing and de serializing a node with
 * EaterSpecification as its node specification.
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

    /**
     * Gets the fraction of the eater.
     * @return the fraction
     */
    public int getFraction() {
        return fraction;
    }
    
    /**
     * Sets the fraction of the eater.
     * @param fraction the fraction
     */
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
