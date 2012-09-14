package simplex.entity;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Image;
import simplex.util.ImageManager;

/**
 * The Factory Specification describes the inner workings of a factory.
 * 
 * @author Emil
 * @author Samuel
 */
public class ConsumerSpecification implements NodeSpecification {

    private List<Connection> inConn = new LinkedList<>();
    private int expected_type;
    private int expected_rate;
    private boolean happy = false;

    public ConsumerSpecification(int type, int rate) {
        this.expected_type = type;
        this.expected_rate = rate;
    }

    @Override
    public boolean addIncomingConnection(Connection conn) {
        inConn.add(conn);
        return true;
    }

    @Override
    public boolean addOutgoingConnection(Connection conn) {
        return false;
    }

    @Override
    public boolean removeIncommingConnection(Connection conn) {
        inConn.remove(conn);
        return true;
    }

    @Override
    public boolean removeOutgoingConnection(Connection conn) {
        return false;
    }

    @Override
    public Image getImage() {
        if (happy) {
            return ImageManager.happy_consumer_node;
        } else {
            return ImageManager.consumer_node;
        }
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
        }

        happy = type == expected_type && rate >= expected_rate;

    }
}
