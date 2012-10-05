package simplex.level.saved;

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

    public SavedFactory(Resource resource, GridCoord coord, int id) {
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
        node.getNodeSpecification().setResource(resource);
        return node;
    }    
}
