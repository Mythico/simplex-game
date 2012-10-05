package simplex.entity.specification;

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

    private Resource resource = Resource.NIL;
    
    @Override
    public void setResource(Resource other) {
        resource = Resource.combine(resource, other);
    }

    @Override
    public Resource getResource() {
        return resource;
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
