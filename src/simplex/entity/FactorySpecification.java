package simplex.entity;

import org.newdawn.slick.Image;
import simplex.util.ImageManager;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class FactorySpecification implements NodeSpecification {

    private Connection outConn = null;
    private int type;
    private int rate;

    public FactorySpecification(int type, int rate) {
        this.type = type;
        this.rate = rate;
    }

    @Override
    public boolean addIncomingConnection(Connection conn) {
        return false;
    }

    @Override
    public boolean addOutgoingConnection(Connection conn) {
        if (outConn == null) {
            outConn = conn;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeIncommingConnection(Connection conn) {
        return false;
    }

    @Override
    public boolean removeOutgoingConnection(Connection conn) {
        if (outConn == null) {
            return false;
        }
        outConn = null;
        return true;
    }
    
    @Override
    public Image getImage() {
        return ImageManager.factory_node;
    }
    
    @Override
    public void update(int delta) {
        if (outConn != null) {
            outConn.setResourceType(type);
            outConn.setResourceRate(rate);
        }
    }
}
