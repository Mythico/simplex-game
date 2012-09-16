package simplex.entity;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Image;
import simplex.util.ImageManager;

/**
 * The eater specification is a simple node specification that allows a single
 * incoming and a single outgoing connection. It eats a fraction of the
 * resources that pass through it.
 *
 * @author Emil
 * @author Samuel
 */
public class SplitterSpecification implements NodeSpecification {

    private List<Connection> inConn = new LinkedList<>();
    private List<Connection> outConn = new LinkedList<>();

    @Override
    public boolean addIncomingConnection(Connection conn) {
        inConn.add(conn);
        return true;
    }

    @Override
    public boolean addOutgoingConnection(Connection conn) {
        outConn.add(conn);
        return true;
    }

    @Override
    public boolean removeIncommingConnection(Connection conn) {
        inConn.remove(conn);
        return true;
    }

    @Override
    public boolean removeOutgoingConnection(Connection conn) {
        outConn.remove(conn);
        return true;
    }

    @Override
    public Image getImage() {
        return ImageManager.dummy_node;
    }

    @Override
    public void update(int delta) {
        int rate = 0;
        int type = 0;

        if (inConn != null) {
            for (Connection conn : inConn) {
                rate += conn.getResourceRate();
                type = conn.getResourceType();
            }
            if (outConn != null) {
                for (Connection conn : outConn) {
                    conn.setResourceRate(rate / outConn.size());
                    conn.setResourceType(type);
                }
            }
        }

    }
}
