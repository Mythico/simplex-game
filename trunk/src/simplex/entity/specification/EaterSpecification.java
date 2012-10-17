package simplex.entity.specification;

import simplex.entity.Resource;

/**
 * The eater specification is a simple node specification that allows a single
 * incoming and a single outgoing connection. It eats a fraction of the
 * resources that pass through it.
 *
 * @author Emil
 * @author Samuel
 */
public class EaterSpecification extends NodeSpecification {

    private int fraction = 1;

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }

    public int getFraction() {
        return fraction;
    }

    @Override
    public void setResource(Resource resource) {
        if (Resource.NIL == resource) {
            return;
        }
        int rate = resource.getRate() / fraction;
        if (rate > 0) {
            resource.setRate(rate);
            notifyObservers(resource);
        }
    }
}
