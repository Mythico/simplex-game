package simplex.level;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import simplex.entity.Connection;
import simplex.entity.Node;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class LevelFileHandler {

    private final File file;

    public LevelFileHandler(String filename) throws IOException {
        file = new File(filename);
    }

    public void saveLevel(Level level) {
        Map<GridCoord, Node> nodes = level.getNodes();
        List<Connection> connections = level.getConnections();

        if (nodes.isEmpty()) {
            return;
        }
        List<SavedNode> savedNodes = createSavedNodes(nodes);
        List<SavedConnection> savedConn = createSavedConnections(connections);

        try {
            FileWriter fw = new FileWriter(file);
            List list = new LinkedList();
            list.addAll(savedNodes);
            list.addAll(savedConn);
            Yaml yaml = new Yaml();
            yaml.dumpAll(list.iterator(), fw);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public Level loadLevel() {
        Level level = new Level();
        List<SavedNode> savedNodes = new LinkedList<>();
        List<SavedConnection> savedConnections = new LinkedList<>();
        try {
            FileReader fr = new FileReader(file);
            Yaml yaml = new Yaml();
            for (Object o : yaml.loadAll(fr)) {
                if (o instanceof SavedConnection) {
                    savedConnections.add((SavedConnection) o);
                } else if (o instanceof SavedNode) {
                    savedNodes.add((SavedNode) o);
                }
            }
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }



        Map<GridCoord, Node> nodes = new HashMap<>();
        List<Connection> connections = new LinkedList<>();
        for (SavedNode saved : savedNodes) {
            Node node = saved.create();
            SavedNodeFactory.instance().addNode(saved.getId(), node);
            nodes.put(new GridCoord(saved.getX(), saved.getY()), node);
        }

        for (SavedConnection saved : savedConnections) {
            connections.add(saved.create());
        }


        if (nodes == null) {
            nodes = new HashMap<>();
        }
        if (connections == null) {
            connections = new LinkedList<>();
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