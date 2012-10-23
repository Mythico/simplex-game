package simplex.level.saved;

import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.Resource;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.NodeSpecification;

/**
 * A Saved Consumer used for serializing and de serializing a node with
 * ConsumerSpecification as its node specification.
 *
 * @author Emil
 * @author Samuel
 */
public class SavedConsumer extends SavedNode {
    private Resource resource;

    @SuppressWarnings("unused")
    private SavedConsumer() {
    }

    public SavedConsumer(Node node) {
        super(node);
        NodeSpecification spec = node.getNodeSpecification();
        ConsumerSpecification consumerSpec = (ConsumerSpecification) spec;
        this.resource = consumerSpec.getExpectedResource();
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
