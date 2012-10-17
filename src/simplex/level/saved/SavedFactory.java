package simplex.level.saved;

import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.Resource;
import simplex.entity.specification.FactorySpecification;
import simplex.entity.specification.NodeSpecification;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedFactory extends SavedNode {
    private Resource resource;

    public SavedFactory() {
    }

    public SavedFactory(Node node) {
        super(node);
        final NodeSpecification spec = node.getNodeSpecification();
        FactorySpecification factorySpec = (FactorySpecification) spec;
        this.resource = factorySpec.getResource();
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }    

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createFactoryNode();
        node.setGridPosition(getCoord());
        node.getNodeSpecification().setResource(resource);
        return node;
    }    
}
