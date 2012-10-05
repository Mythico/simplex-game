package simplex.entity.specification;

import java.util.HashMap;
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
public class EaterSpecification implements NodeSpecification {

    private Resource resource = Resource.NIL;
    private int fraction = 1;

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }

    public int getFraction() {
        return fraction;
    }

    @Override
    public void setResource(Resource other) {
        resource = Resource.combine(resource, other);
        resource.setRate(resource.getRate()/fraction);
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final EaterSpecification other = (EaterSpecification) obj;
        if (!Objects.equals(this.resource, other.resource)) {
            return false;
        }
        return true;
    }
}
