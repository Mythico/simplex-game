package simplex.entity.specification;

import simplex.entity.Resource;

/**
 * The dummy specification is a simple node specification that allows a single
 * incoming and a single outgoing connection. It's is used in development for
 * creating simple nodes.
 *
 * @author Emil
 * @author Samuel
 */
public class DummySpecification extends NodeSpecification {
    
    @Override
    public void setResource(Resource resource) {
        if(resource != Resource.NIL){
            notifyObservers(resource);
        }
    }    
}
