package simplex.level;

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
public final class LevelFileHandler{
    
    private static LevelFileHandler instance;
    private String setLevel = "default_level.yml";

    
    public static LevelFileHandler instance() {
        if(instance == null){
            instance = new LevelFileHandler();
        }
        return instance;
    }    
    
    public void setLevel(String level){
        setLevel = level;
    }
    
    public void saveLevel(Level level, String filename) {    
        Map<GridCoord, Node> nodes = level.getNodes();
        List<Connection> connections = level.getConnections();

        if (nodes.isEmpty()) {
            return;
        }
        List<SavedNode> savedNodes = createSavedNodes(nodes);
        List<SavedConnection> savedConn = createSavedConnections(connections);

        try (FileWriter fw = new FileWriter("level/" + filename)) {
            List<SavedEntity> list = new LinkedList<SavedEntity>();
            list.addAll(savedNodes);
            list.addAll(savedConn);
            Yaml yaml = new Yaml();
            yaml.dumpAll(list.iterator(), fw);
        } catch (IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }
    
    public Level loadLevel(){
        return loadLevel(setLevel);
    }

    public Level loadLevel(String filename) { 
        Level level = new Level();
        List<SavedEntity> savedObjects = new LinkedList<>();
        try (FileReader fr = new FileReader("level/" +filename)) {
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
