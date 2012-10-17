package simplex.entity.specification;

import simplex.entity.Resource;

/**
 * A simple observer interface for observing incomming resources.
 *
 * @author Emil
 * @author Samuel
 */
public interface Observer{

    /**
     * Updates with an resource.
     * @param r The resource the observer will be updated with
     */
    public void update(Resource r);
    
}
