package simplex.entity;

/**
 * An interface for node specification. The specification will describe
 * how the node will process resources.
 *
 * @author Emil
 * @author Samuel
 */
public interface NodeSpecification {
    
    
    /**
     * Set the nodes resource.
     * @param resource An incomming resource
     */
    public void setResource(Resource resource);
    
    /**
     * Returns the resource this node has.
     * @return This nodes resource.
     */
    public Resource getResource();
    
}
