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
public class CombinerSpecification extends NodeSpecification {

    private Resource bufferedResource = Resource.NIL;

    @Override
    public void setResource(Resource other) {
        if (Resource.NIL == other) {
            return;
        }

        if (bufferedResource == Resource.NIL) {
            bufferedResource = other;
        } else {
            final int type1 = bufferedResource.getType();
            final int type2 = other.getType();
            other.setRate(other.getRate() + bufferedResource.getRate());
            if (type1 == Resource.RED && type2 == Resource.BLUE
                    || type1 == Resource.BLUE && type2 == Resource.RED) {
                other.setType(Resource.GREEN);
            } else if (type1 == Resource.RED && type2 == Resource.GREEN
                    || type1 == Resource.GREEN && type2 == Resource.RED) {
                other.setType(Resource.BLUE);
            } else if (type1 == Resource.GREEN && type2 == Resource.BLUE
                    || type1 == Resource.BLUE && type2 == Resource.GREEN) {
                other.setType(Resource.RED);
            }
            bufferedResource = Resource.NIL;
            notifyObservers(other);
        }
    }
}