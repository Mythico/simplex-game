package simplex.level.saved;

import simplex.entity.Connection;

/**
 *
 * @author Emil
 * @author Samuel
 */
public final class SavedConnectionFactory {
    private static SavedConnectionFactory instance = null;

    private SavedConnectionFactory(){}
    public static SavedConnectionFactory instance() {
        if (instance == null) {
            instance = new SavedConnectionFactory();
        }
        return instance;
    }
    private int id;

    public SavedConnection create(Connection conn) {
        id++;
        int startId = SavedNodeFactory.instance().getId(conn.getStartNode());
        int endId = SavedNodeFactory.instance().getId(conn.getEndNode());
        return new SavedConnection(startId, endId, id);
    }
}
