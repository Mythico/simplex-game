package simplex.entity;

import org.newdawn.slick.Image;
import simplex.util.ImageManager;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class FactorySpecification implements NodeSpecification {

    private Resource resource = new Resource();

    @Override
    public Image getImage() {
        return ImageManager.factory_node;
    }

    @Override
    public void setResource(Resource resource, Connection conn) {
        if (conn != null) {
            throw new UnsupportedOperationException("Factories dosn't support incomming connection.");
        }
        this.resource = resource;
    }

    @Override
    public Resource getResource() {
        return resource;
    }
}
