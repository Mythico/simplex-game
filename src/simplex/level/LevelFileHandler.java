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
import simplex.level.saved.SavedEntity;
import simplex.level.saved.SavedNode;
import simplex.level.saved.SavedNodeFactory;
import simplex.util.GridCoord;

/**
 * The level file handler handle loading and saving of levels.
 * To load a level, you will first have to set the level name and then load it
 * or load the level with a name as a parameter.
 * The first way of loading is there for easier communicate between different
 * StateBasedGames since you can't send messages between states when loaded.
 *
 * @author Emil
 * @author Samuel
 */
public final class LevelFileHandler{
    
    private static LevelFileHandler instance;
    private String setLevel = "default_level";

    
    public static LevelFileHandler instance() {
        if(instance == null){
            instance = new LevelFileHandler();
        }
        return instance;
    }    
    
    /**
     * Save a level.
     * @param level The level that will be saved.
     * @param filename The filename of said level.
     */
    public void saveLevel(Level level, String filename) {    
        Map<GridCoord, Node> nodes = level.getNodes();
        List<Connection> connections = level.getConnections();

        if (nodes.isEmpty()) {
            return;
        }
        List<SavedNode> savedNodes = createSavedNodes(nodes);
        List<SavedConnection> savedConn = createSavedConnections(connections);

        try (FileWriter fw = new FileWriter("level/" + filename + ".yml")) {
            List<SavedEntity> list = new LinkedList<>();
            list.addAll(savedNodes);
            list.addAll(savedConn);
            Yaml yaml = new Yaml();
            yaml.dumpAll(list.iterator(), fw);
        } catch (IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }
    
    /**
     * Set the name of the level that will be loaded.
     * @param level The name of the level.
     */
    public void setLevel(String level){
        setLevel = level;
    }
    
    /**
     * Load the level specified with setLevel(String level)
     * @return A level.
     */
    public Level loadLevel(){
        return loadLevel(setLevel);
    }

    /**
     * Load a level.
     * @param filename The name of the level.
     * @return A level.
     */
    public Level loadLevel(String filename) { 
        Level level = new Level(filename);
        List<SavedEntity> savedObjects = new LinkedList<>();
        try (FileReader fr = new FileReader("level/" +filename + ".yml")) {
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

    /**
     * Create a list of saved Connections
     * @param conns The connections that is being saved.
     * @return A list of saved connections.
     */
    private List<SavedConnection> createSavedConnections(List<Connection> conns) {
        List<SavedConnection> savedConnections = new LinkedList<>();
        for (Connection conn : conns) {
            savedConnections.add(new SavedConnection(conn));
        }
        return savedConnections;
    }

    /**
     * Create a list of saved Nodes
     * @param conns The nodes that is being saved.
     * @return A list of saved nodes.
     */
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
