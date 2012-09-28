package simplex.level;

import simplex.entity.ConsumerSpecification;
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.Resource;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedConsumer extends SavedNode {
    private Resource resource;

    private SavedConsumer() {
    }

    SavedConsumer(Resource resource, int x, int y, int id) {
        super(x, y, id);
        this.resource = resource;
    }

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createConsumerNode();
        node.setGridPosition(new GridCoord(getX(), getY()));
        ConsumerSpecification spec = (ConsumerSpecification) node.getNodeSpecification();
        spec.setExpectedResource(resource);
        return node;
    }    
}
