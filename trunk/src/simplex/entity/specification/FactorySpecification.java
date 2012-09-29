package simplex.entity.specification;

import simplex.entity.Resource;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class FactorySpecification implements NodeSpecification {

    private Resource resource = Resource.NIL;

    @Override
    public void setResource(Resource resource) {        
        this.resource = resource;                
    }

    @Override
    public Resource getResource() {
        return resource;
    }
}
