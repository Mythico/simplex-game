package simplex.level;

import simplex.entity.specification.EaterSpecification;
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedEater extends SavedNode {
    private int fraction;

    private SavedEater() {
    }

    SavedEater(int fraction, GridCoord coord, int id) {
        super(coord, id);
        this.fraction = fraction;
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