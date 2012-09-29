package simplex.level;

import java.util.HashMap;
import java.util.Map;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.FactorySpecification;
import simplex.entity.Node;
import simplex.entity.specification.NodeSpecification;
import simplex.entity.Resource;
import simplex.entity.specification.SplitterSpecification;
import simplex.util.GridCoord;

/** 
 * A factory for creating SavedNodes.
 * 
 * @author Emil
 * @author Samuel
 */
final class SavedNodeFactory {

    private static SavedNodeFactory instance = null;
    private Map<Integer, Node> savedNodes = new HashMap<>();
    private Map<Node, Integer> savedIds = new HashMap<>();

    private SavedNodeFactory(){}
    
    static SavedNodeFactory instance() {
        if (instance == null) {
            instance = new SavedNodeFactory();
        }
        return instance;
    }
    private int id;

    SavedNode create(GridCoord coord, Node node) {
        id++;
        addNode(id, node);
        final NodeSpecification spec = node.getNodeSpecification();
        if (spec instanceof FactorySpecification) {
            final Resource res = spec.getResource();
            return new SavedFactory(res, coord, id);
        } else if (spec instanceof ConsumerSpecification) {
            final Resource res = spec.getResource();
            return new SavedConsumer(res, coord, id);
        } else if (spec instanceof EaterSpecification) {
            final int fraction = ((EaterSpecification) spec).getFraction();
            return new SavedEater(fraction, coord, id);
        } else if (spec instanceof SplitterSpecification) {
            return new SavedSplitter(coord, id);
        } else {
            return new SavedDummy(coord, id);
        }
    }
    
    Node getNode(int id) {
        return savedNodes.get(id);
    }
    
    int getId(Node node){
        return savedIds.get(node);        
    }

    void addNode(int id, Node node) {
        savedNodes.put(id, node);
        savedIds.put(node, id);
    }

}
