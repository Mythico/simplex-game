package simplex.entity.specification;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import simplex.entity.Resource;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class FactorySpecification extends NodeSpecification {

    private Resource resource = Resource.NIL;
    private final Timer timer = new Timer();
    private final TimerTask spawnResources = new TimerTask() {
        @Override
        public void run() {
            if (resource != Resource.NIL) {
                notifyObservers(resource.copy());
            }
        }
    };

    public FactorySpecification() {
        final long period = 1000; //ms
        timer.scheduleAtFixedRate(spawnResources, period, period);
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.resource);
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
