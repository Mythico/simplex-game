package simplex.entity;

import java.util.HashMap;
import org.newdawn.slick.Image;
import simplex.util.ImageManager;

/**
 * The eater specification is a simple node specification that allows a single
 * incoming and a single outgoing connection. It eats a fraction of the
 * resources that pass through it.
 * 
 * @author Emil
 * @author Samuel
 */
public class EaterSpecification implements NodeSpecification {

    private HashMap<Connection, Resource> resourceMap = new HashMap<>();
    private int fraction = 1;

    

    @Override
    public Image getImage() {
        return ImageManager.dummy_node;
    }

    

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }      

    public int getFraction() {
        return fraction;
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
        resource.setRate(resource.getRate() / fraction);
        return resource;
    }
}
