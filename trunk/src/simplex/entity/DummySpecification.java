package simplex.entity;

import java.util.HashMap;

/**
 * The dummy specification is a simple node specification that allows a single
 * incoming and a single outgoing connection. It's is used in development for
 * creating simple nodes.
 *
 * @author Emil
 * @author Samuel
 */
public class DummySpecification implements NodeSpecification {

    private Resource resource = Resource.NIL;
    
    @Override
    public void setResource(Resource other) {
        resource = Resource.combine(resource, other);
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    
    
}
