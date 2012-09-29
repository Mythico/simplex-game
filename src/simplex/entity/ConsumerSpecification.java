package simplex.entity;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class ConsumerSpecification implements NodeSpecification {

    private Resource resource = Resource.NIL;
    private Resource expectedResource = Resource.NIL;
    private boolean happy = false;

    public Resource getExpectedResource() {
        return expectedResource;
    }

    public void setExpectedResource(Resource expectedResource) {
        this.expectedResource = expectedResource;
    }   

    @Override
    public void setResource(Resource other) {
        resource = Resource.combine(resource, other);
                
        if(!happy && expectedResource.getType() == resource.getType()
                && expectedResource.getRate() <= resource.getRate()){
            happy = true;
        }
    }

    @Override
    public Resource getResource() {
        return Resource.NIL;
    }

    public boolean isHappy() {
        return happy;
    }
}
