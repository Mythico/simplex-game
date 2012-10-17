package simplex.entity.specification;

import simplex.entity.Resource;

/**
 * An interface for node specification. The specification will describe how the
 * node will process resources.
 *
 * @author Emil
 * @author Samuel
 */
public abstract class NodeSpecification extends Observable {

    /**
     * Set the nodes resource.
     *
     * @param resource An incomming resource
     */
    public abstract void setResource(Resource resource);
}
