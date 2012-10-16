package simplex.entity.specification;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import simplex.entity.Resource;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class FactorySpecification implements NodeSpecification {

    private Resource resource = Resource.NIL;
    private Queue<Resource> resources = new LinkedList<>();
    private TimerTask spawn = new TimerTask() {

        @Override
        public void run() {
            if(resource != Resource.NIL){
                resources.add(resource.copy());
            }
        }
    };
    private Timer timer = new Timer();

    public FactorySpecification() {        
        long period = 1000;
        timer.scheduleAtFixedRate(spawn, period, period);
    }

    
    
    @Override
    public void setResource(Resource resource) {        
        this.resource = resource;        
    }

    @Override
    public Resource getResource() {
        if(resources.isEmpty()){
            return Resource.NIL;
        }
        return resources.poll();
    }
    
    public Resource getExpectedResource(){
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
