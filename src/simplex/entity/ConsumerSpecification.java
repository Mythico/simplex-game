package simplex.entity;

import org.newdawn.slick.Image;
import simplex.util.ImageManager;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class ConsumerSpecification implements NodeSpecification {

    private Connection inConn = null;
    private int type;
    private int rate;
    private boolean happy = false;

    public ConsumerSpecification(int type, int rate) {
        this.type = type;
        this.rate = rate;
    }

    @Override
    public boolean addOutgoingConnection(Connection conn) {
        return false;
    }

    @Override
    public boolean addIncomingConnection(Connection conn) {
        if (inConn == null) {
            inConn = conn;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeOutgoingConnection(Connection conn) {
        return false;
    }

    @Override
    public boolean removeIncommingConnection(Connection conn) {
        if (inConn == null) {
            return false;
        }
        inConn = null;
        return true;
    }

    @Override
    public Image getImage() {
        if(happy){
            return ImageManager.happy_consumer_node;
        } else {
            return ImageManager.consumer_node;            
        }
    }

    @Override
    public void update(int delta) {
        happy = inConn.getResourceType() == type
                && inConn.getResourceRate() >= rate;

    }
}
