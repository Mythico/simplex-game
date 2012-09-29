package simplex.entity.specification;

import java.util.HashMap;
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
}
