package simplex.entity;

import org.newdawn.slick.Image;
import simplex.util.ImageManager;

/**
 * The dummy specification is a simple node specification that allows a single
 * incoming and a single outgoing connection. It's is used in development for
 * creating simple nodes.
 *
 * @author Emil
 * @author Samuel
 */
public class DummySpecification implements NodeSpecification {

    private Connection inConn = null;
    private Connection outConn = null;

    @Override
    public boolean addIncomingConnection(Connection conn) {
        if (inConn == null) {
            inConn = conn;
            return true;
        }
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
        if (inConn == null) {
            return false;
        }
        inConn = null;
        return true;
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
        return ImageManager.dummy_node;
    }

    @Override
    public void update(int delta) {
        if (inConn != null) {
            int type = inConn.getResourceType();
            int rate = inConn.getResourceRate();

            if (outConn != null) {
                outConn.setResourceType(type);
                outConn.setResourceRate(rate);
            }
        }
    }
    
}
