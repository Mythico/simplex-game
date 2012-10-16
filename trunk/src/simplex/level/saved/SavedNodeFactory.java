package simplex.level.saved;

import java.util.HashMap;
import java.util.Map;
import simplex.entity.Node;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.FactorySpecification;
import simplex.entity.specification.NodeSpecification;
import simplex.entity.specification.SplitterSpecification;
import simplex.util.GridCoord;

/** 
 * A factory for creating SavedNodes.
 * 
 * @author Emil
 * @author Samuel
 */
public final class SavedNodeFactory {

    private static SavedNodeFactory instance = null;
    private Map<Integer, Node> savedNodes = new HashMap<>();
    private Map<Node, Integer> savedIds = new HashMap<>();

    private SavedNodeFactory(){}
    
    public static SavedNodeFactory instance() {
        if (instance == null) {
            instance = new SavedNodeFactory();
        }
        return instance;
    }

    public SavedNode create(GridCoord coord, Node node) {
        addNode(node.getId(), node);
        final NodeSpecification spec = node.getNodeSpecification();
        if (spec instanceof FactorySpecification) {
            return new SavedFactory(node);
        } else if (spec instanceof ConsumerSpecification) {            
            return new SavedConsumer(node);
        } else if (spec instanceof EaterSpecification) {
            return new SavedEater(node);
        } else if (spec instanceof SplitterSpecification) {
            return new SavedSplitter(node);
        } else {
            return new SavedDummy(node);
        }
    }
    
    Node getNode(int id) {
        return savedNodes.get(id);
    }
    
    int getId(Node node){
        return savedIds.get(node);        
    }

    public void addNode(int id, Node node) {
        savedNodes.put(id, node);
        savedIds.put(node, id);
    }

}
