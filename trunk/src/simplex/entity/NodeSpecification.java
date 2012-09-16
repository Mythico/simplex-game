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
     * Adds an incoming connection and return true if successful.
     * @param conn A connection to be added.
     * @return Returns true if successful, otherwise false;
     */
    public boolean addIncomingConnection(Connection conn);
    
    /**
     * Adds an outgoing connection and return true if successful.
     * @param conn A connection to be added.
     * @return Returns true if successful, otherwise false;
     */
    public boolean addOutgoingConnection(Connection conn);
    
    /**
     * Removes an incoming connection and return true if successful.
     * @param conn A connection to be added.
     * @return Returns true if successful, otherwise false;
     */
    public boolean removeIncommingConnection(Connection conn);

    /**
     * Removes an outgoing connection and return true if successful.
     * @param conn A connection to be added.
     * @return Returns true if successful, otherwise false;
     */
    public boolean removeOutgoingConnection(Connection conn);
  
    /**
     * Returns the graphical representation of the specification.
     * @return An image that represent the specification.
     */
    public Image getImage();
    
    
    /**
     * Collects resources from the incoming connections, process them and
     * send them to the outgoing resources.
     */
    public void update(int delta);
    
}
