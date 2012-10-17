package simplex.entity.specification;

import java.util.Objects;
import simplex.entity.Resource;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class ConsumerSpecification extends NodeSpecification {

    private Resource expectedResource = Resource.NIL;
    private boolean happy = false;

    public Resource getExpectedResource() {
        return expectedResource;
    }

    public void setExpectedResource(Resource expectedResource) {
        this.expectedResource = expectedResource;
    }

    @Override
    public void setResource(Resource resource) {
        happy = (expectedResource.getType() == resource.getType()
                && expectedResource.getRate() <= resource.getRate());
    }

    public boolean isHappy() {
        return happy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.expectedResource);
        hash = 61 * hash + (this.happy ? 1 : 0);
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
        final ConsumerSpecification other = (ConsumerSpecification) obj;
        if (!Objects.equals(this.expectedResource, other.expectedResource)) {
            return false;
        }
        if (this.happy != other.happy) {
            return false;
        }
        return true;
    }
}
