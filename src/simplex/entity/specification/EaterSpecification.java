package simplex.entity.specification;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
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

    private Queue<Resource> resource = new LinkedList<>();
    private int fraction = 1;
    private int counter = 0;

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }

    public int getFraction() {
        return fraction;
    }

    @Override
    public void setResource(Resource other) {
        if (Resource.NIL == other) {
            return;
        }
        int rate = other.getRate() / fraction;
        if (rate > 0) {
            other.setRate(rate);
            resource.add(other);
        }
    }

    @Override
    public Resource getResource() {
        if (resource.isEmpty()) {
            return Resource.NIL;
        }
        return resource.poll();
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
