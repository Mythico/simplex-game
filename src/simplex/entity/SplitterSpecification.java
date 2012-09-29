package simplex.entity;

import java.util.ArrayList;
import java.util.HashMap;

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
}
