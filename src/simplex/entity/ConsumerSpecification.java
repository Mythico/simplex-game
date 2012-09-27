package simplex.entity;

import java.util.HashMap;
import org.newdawn.slick.Image;
import simplex.util.ImageManager;

/**
 * The Factory Specification describes the inner workings of a factory.
 * 
 * @author Emil
 * @author Samuel
 */
public class ConsumerSpecification implements NodeSpecification {

    private HashMap<Connection, Resource> resourceMap = new HashMap<>();
    private Resource expectedResource = new Resource();
    private boolean happy = false;


    @Override
    public Image getImage() {
        if (happy) {
            return ImageManager.happy_consumer_node;
        } else {
            return ImageManager.consumer_node;
        }
    }

    public void setExpectedResource(Resource expectedResource) {
        this.expectedResource = expectedResource;
    }

  

    @Override
    public void setResource(Resource resource, Connection conn) {        
        resourceMap.put(conn, resource);
        Resource re = new Resource();
        for(Resource r : resourceMap.values()){
            re.add(r);
        }
        happy = expectedResource.equals(re);
    }

    @Override
    public Resource getResource() {
        throw new UnsupportedOperationException("Consumers doesn't support outgoing connections.");
    }
}
