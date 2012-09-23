package simplex.entity;

import org.newdawn.slick.Image;

/**
 * An interface for node specification. The specification will describe
 * how the node will process resources.
 *
 * @author Emil
 * @author Samuel
 */
public interface NodeSpecification {
    
    /**
     * Returns the graphical representation of the specification.
     * @return An image that represent the specification.
     */
    public Image getImage();
    
    /**
     * Set an incomming resource and couple it with the incomming connection.
     * @param resource An incomming resource
     * @param conn The connection transporting the resource
     */
    public void setResource(Resource resource, Connection conn);
    
    /**
     * Returns the resource this node has.
     * @return This nodes resource.
     */
    public Resource getResource();
    
}
