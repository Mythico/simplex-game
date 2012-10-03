package simplex.level.saved;

import simplex.entity.specification.FactorySpecification;
import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.entity.Resource;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedFactory extends SavedNode {
    private Resource resource;

    public SavedFactory() {
    }

    SavedFactory(Resource resource, GridCoord coord, int id) {
        super(coord, id);
        this.resource = resource;
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
        FactorySpecification spec = (FactorySpecification) node.getNodeSpecification();
        spec.setResource(resource);
        return node;
    }    
}
