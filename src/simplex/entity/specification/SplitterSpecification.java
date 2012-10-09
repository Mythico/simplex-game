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
public class SplitterSpecification implements NodeSpecification {

    private Queue<Resource> resource = new LinkedList<>();

    @Override
    public void setResource(Resource other) {
        if (Resource.NIL == other) {
            return;
        }
        int rate = other.getRate();
        if (other.getType() == Resource.RED) {
            resource.add(Resource.parse(Resource.BLUE, rate));
            resource.add(Resource.parse(Resource.GREEN, rate));
        } else if (other.getType() == Resource.GREEN) {
            resource.add(Resource.parse(Resource.BLUE, rate));
            resource.add(Resource.parse(Resource.RED, rate));
        } else if (other.getType() == Resource.BLUE) {
            resource.add(Resource.parse(Resource.GREEN, rate));
            resource.add(Resource.parse(Resource.RED, rate));
        }
    }

    @Override
    public Resource getResource() {
        if(resource.isEmpty()){
            return Resource.NIL;
        }
        return resource.poll();
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
