package simplex.level;

import simplex.entity.FactorySpecification;
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

    SavedFactory(Resource resource, int x, int y, int id) {
        super(x, y, id);
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
        node.setGridPosition(new GridCoord(getX(), getY()));
        FactorySpecification spec = (FactorySpecification) node.getNodeSpecification();
        spec.setResource(resource, null);
        return node;
    }    
}
