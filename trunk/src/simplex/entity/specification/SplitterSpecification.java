package simplex.entity.specification;

import java.util.ArrayList;
import java.util.Objects;
import simplex.entity.Resource;

/**
 * The eater specification is a simple node specification that allows a single
 * incoming and a single outgoing connection. It eats a fraction of the
 * resources that pass through it.
 *
 * @author Emil
 * @author Samuel
 */
public class SplitterSpecification implements NodeSpecification {

    private Resource resource = Resource.NIL;
    private int iterator = 0;

    @Override
    public void setResource(Resource other) {
        resource = Resource.combine(resource, other);
    }

    @Override
    public Resource getResource() {
        ArrayList<Resource> values = Resource.split(resource);
        if (values.isEmpty()) {
            return null;
        }
        iterator++;
        if (iterator >= values.size()) {
            iterator = 0;
        }
        return values.get(iterator);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.resource);
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
        final SplitterSpecification other = (SplitterSpecification) obj;
        if (!Objects.equals(this.resource, other.resource)) {
            return false;
        }
        return true;
    }

    
}
