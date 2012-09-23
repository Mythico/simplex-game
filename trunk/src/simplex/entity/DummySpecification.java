package simplex.entity;

import java.util.HashMap;
import org.newdawn.slick.Image;
import simplex.util.ImageManager;

/**
 * The dummy specification is a simple node specification that allows a single
 * incoming and a single outgoing connection. It's is used in development for
 * creating simple nodes.
 *
 * @author Emil
 * @author Samuel
 */
public class DummySpecification implements NodeSpecification {

    private HashMap<Connection, Resource> resourceMap = new HashMap<>();
    

    @Override
    public Image getImage() {
        return ImageManager.dummy_node;
    }

    @Override
    public void setResource(Resource resource, Connection conn) {
        resourceMap.put(conn, resource);
    }

    @Override
    public Resource getResource() {
        Resource resource = new Resource();
        for(Resource r : resourceMap.values()){
            resource.add(r);
        }
        return resource;
    }

    
    
}
