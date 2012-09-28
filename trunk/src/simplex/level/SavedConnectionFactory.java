package simplex.level;

import simplex.entity.Connection;

/**
 *
 * @author Emil
 * @author Samuel
 */
final class SavedConnectionFactory {
    private static SavedConnectionFactory instance = null;

    private SavedConnectionFactory(){}
    static SavedConnectionFactory instance() {
        if (instance == null) {
            instance = new SavedConnectionFactory();
        }
        return instance;
    }
    private int id;

    SavedConnection create(Connection conn) {
        id++;
        int startId = SavedNodeFactory.instance().getId(conn.getStartNode());
        int endId = SavedNodeFactory.instance().getId(conn.getEndNode());
        return new SavedConnection(startId, endId, id);
    }
}
