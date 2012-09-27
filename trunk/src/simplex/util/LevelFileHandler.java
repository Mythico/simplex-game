/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yaml.snakeyaml.Yaml;
import simplex.entity.Connection;
import simplex.entity.Node;
import sun.font.CreatedFontTracker;

/**
 *
 * @author Samuel
 */
public class LevelFileHandler {

    private static Map<Integer, IdPair> pairMap = new HashMap<>();
    
    private static <T> void saveToFile(T data,
            String filename, boolean append) {

        File f = new File(filename);
        Yaml yaml = new Yaml();
        try {
            final FileWriter out = new FileWriter(filename, append);
            yaml.dump(data, out);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Map<GridCoord, Node> tmp_nodes;
    private static List<Connection> tmp_connections;

    public static void saveNodesToFile(Map<GridCoord, Node> nodes,
            String filename, boolean append) {

        tmp_nodes = new HashMap<>(nodes);
        /*
        if (nodes.isEmpty()) {
            return;
        }       
        try {
            ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(filename, append));
            oo.writeObject(nodes);
            oo.close();
        } catch (IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
             /*
             List<Node> list = new ArrayList(nodes.values());
             
             for(Node node : list){
                 IdPair<Node> pair = IdPair.<Node>createPair(node);
                 pairMap.put(pair.getId(), pair);                    
             }
             
             saveToFile(list, filename, append);
             * */
    }

    public static void saveConnectionsToFile(List<Connection> connections,
            String filename, boolean append) {

        tmp_connections = new LinkedList<>(connections);
        /*
        if (connections.isEmpty()) {
            return;
        }
        try {
            ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(filename, append));
            oo.writeObject(connections);
            oo.close();
        } catch (IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        saveToFile(connections, filename, append);
        */

    }

    public static Map<GridCoord, Node> loadNodesFromFile(String filename) {
        /*Map<GridCoord, Node> nodes = null;
        
        try {
            ObjectInputStream oo = new ObjectInputStream(new FileInputStream(filename));
            nodes = (Map<GridCoord, Node>) oo.readObject();
            oo.close();
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }/*
        Yaml yaml = new Yaml();
        
        try {
            Iterable<Object> iter = yaml.loadAll(new FileInputStream(filename));
            for (Object o : iter) {
                if (o instanceof Map) {
                    nodes = (Map<GridCoord, Node>) o;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        Map<GridCoord, Node> nodes = tmp_nodes;
        if (nodes == null) {
            nodes = new HashMap<>();
        }
        return nodes;        
    }

    public static List<Connection> loadConnectionsFromFile(String filename) {
        /*List<Connection> connections = null;
        try {
            ObjectInputStream oo = new ObjectInputStream(new FileInputStream(filename));
            connections = (List<Connection>) oo.readObject();
            oo.close();
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*
        Yaml yaml = new Yaml();
        try {
            Iterable<Object> iter = yaml.loadAll(new FileInputStream(filename));
            for (Object o : iter) {
                if (o instanceof List) {
                    connections = (List<Connection>) o;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        List<Connection> connections = tmp_connections;
        if (connections == null) {
            connections = new LinkedList<>();
        }
        return connections;
    }
}

class IdPair<T> {

    private final int id;
    private final T object;
    private static int nr = 0;

    public static <T> IdPair<T> createPair(T object) {
        return new IdPair(nr++, object);
    }

    private IdPair(int id, T object) {
        this.id = id;
        this.object = object;
    }

    public int getId() {
        return id;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    /**
     * Check if an Integer is equal to the id or
     * if an Object obj is equal to the object.
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (Integer.class == obj.getClass()) {
            return this.id == ((Integer)obj).intValue();        
        }
        if(Objects.equals(obj, object)){
            return true;
        }
        return false;
    }
}