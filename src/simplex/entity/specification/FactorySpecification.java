package simplex.entity.specification;

import java.util.Objects;
import simplex.entity.Resource;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class FactorySpecification implements NodeSpecification {

    private Resource resource = Resource.NIL;

    @Override
    public void setResource(Resource resource) {        
        this.resource = resource;                
    }

    @Override
    public Resource getResource() {
        return resource;
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
        final FactorySpecification other = (FactorySpecification) obj;
        if (!Objects.equals(this.resource, other.resource)) {
            return false;
        }
        return true;
    }
    
    
}
