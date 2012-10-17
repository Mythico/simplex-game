package simplex.entity.specification;

import java.util.LinkedList;
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
public class SplitterSpecification extends NodeSpecification {

    @Override
    public void setResource(Resource other) {
        if (Resource.NIL == other) {
            return;
        }
        int rate = other.getRate();
        
        Queue<Resource> resources = new LinkedList<>();
        if (other.getType() == Resource.RED) {
            resources.add(Resource.parse(Resource.BLUE, rate));
            resources.add(Resource.parse(Resource.GREEN, rate));
        } else if (other.getType() == Resource.GREEN) {
            resources.add(Resource.parse(Resource.BLUE, rate));
            resources.add(Resource.parse(Resource.RED, rate));
        } else if (other.getType() == Resource.BLUE) {
            resources.add(Resource.parse(Resource.GREEN, rate));
            resources.add(Resource.parse(Resource.RED, rate));
        }
        notifyObservers(resources);
    }
}
