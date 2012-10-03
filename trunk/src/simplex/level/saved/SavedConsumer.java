package simplex.level.saved;

import simplex.entity.specification.ConsumerSpecification;
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

    SavedConsumer(Resource resource, GridCoord coord, int id) {
        super(coord, id);
        this.resource = resource;
    }

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createConsumerNode();
        node.setGridPosition(getCoord());
        ConsumerSpecification spec = (ConsumerSpecification) node.getNodeSpecification();
        spec.setExpectedResource(resource);
        return node;
    }    

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
    
    
}
