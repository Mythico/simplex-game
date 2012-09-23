package simplex.entity;

import java.util.ArrayList;
import java.util.Collection;
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
public class SplitterSpecification implements NodeSpecification {

    private HashMap<Connection, Resource> resourceMap = new HashMap<>();
    private int iterator = 0;

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
        ArrayList<Resource> values = new ArrayList<>(resourceMap.values());
        if(values.isEmpty()){
            return null;
        }
        iterator++;
        if(iterator >= values.size()){
            iterator = 0;
        }            
        return values.get(iterator);
    }
}
