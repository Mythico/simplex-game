package simplex.level;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import simplex.entity.Connection;
import simplex.entity.Node;
import simplex.level.saved.SavedConnection;
import simplex.level.saved.SavedConnectionFactory;
import simplex.level.saved.SavedEntity;
import simplex.level.saved.SavedNode;
import simplex.level.saved.SavedNodeFactory;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class LevelFileHandler {
    
    private static String level_name = "default_level.yml";

    public static void setLevelName(String name){
        level_name = name;
    }
    
    public static String getLevelName() {
        return level_name;
    }
    

    private final File file;

    public LevelFileHandler() throws IOException {
        file = new File("level/" + getLevelName());
    }

    public void saveLevel(Level level) {
        Map<GridCoord, Node> nodes = level.getNodes();
        List<Connection> connections = level.getConnections();

        if (nodes.isEmpty()) {
            return;
        }
        List<SavedNode> savedNodes = createSavedNodes(nodes);
        List<SavedConnection> savedConn = createSavedConnections(connections);

        try (FileWriter fw = new FileWriter(file)) {
            List list = new LinkedList();
            list.addAll(savedNodes);
            list.addAll(savedConn);
            Yaml yaml = new Yaml();
            yaml.dumpAll(list.iterator(), fw);
        } catch (IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    public Level loadLevel() {
        Level level = new Level();
        List<SavedEntity> savedObjects = new LinkedList<>();
        try (FileReader fr = new FileReader(file)) {
            Yaml yaml = new Yaml();
            for (Object o : yaml.loadAll(fr)) {
                savedObjects.add((SavedEntity) o);
            }
        } catch (IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }


        Map<GridCoord, Node> nodes = new HashMap<>();
        List<Connection> connections = new LinkedList<>();
        for (SavedEntity saved : savedObjects) {
            if (saved instanceof SavedNode) {
                SavedNode snode = (SavedNode) saved;
                Node node = snode.create();
                SavedNodeFactory.instance().addNode(saved.getId(), node);
                nodes.put(snode.getCoord(), node);
            } else if (saved instanceof SavedConnection) {
                SavedConnection sconn = (SavedConnection) saved;
                connections.add(sconn.create());
            }
        }

        level.setNodes(nodes);
        level.setConnections(connections);
        return level;
    }

    private List<SavedConnection> createSavedConnections(List<Connection> conns) {
        List<SavedConnection> savedConnections = new LinkedList<>();
        SavedConnectionFactory factory = SavedConnectionFactory.instance();
        for (Connection conn : conns) {
            SavedConnection savedConn = factory.create(conn);
            savedConnections.add(savedConn);
        }
        return savedConnections;
    }

    private List<SavedNode> createSavedNodes(Map<GridCoord, Node> nodes) {
        List<SavedNode> savedNodes = new LinkedList<>();
        SavedNodeFactory factory = SavedNodeFactory.instance();
        for (GridCoord coord : nodes.keySet()) {
            Node node = nodes.get(coord);
            SavedNode saved = factory.create(coord, node);
            savedNodes.add(saved);
        }
        return savedNodes;
    }
}
