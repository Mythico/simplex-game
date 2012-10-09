package simplex.entity.specification;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import simplex.entity.Resource;

/**
 * The dummy specification is a simple node specification that allows a single
 * incoming and a single outgoing connection. It's is used in development for
 * creating simple nodes.
 *
 * @author Emil
 * @author Samuel
 */
public class DummySpecification implements NodeSpecification {

    private Deque<Resource> resource = new LinkedList<>();
    
    @Override
    public void setResource(Resource other) {
        if(resource != Resource.NIL){
            resource.add(other);
        }
    }

    @Override
    public Resource getResource() {
        if(resource.isEmpty()){
            return Resource.NIL;
        }        
        return resource.poll();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.resource);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DummySpecification other = (DummySpecification) obj;
        if (!Objects.equals(this.resource, other.resource)) {
            return false;
        }
        return true;
    }

    
}
