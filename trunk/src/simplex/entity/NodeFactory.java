package simplex.entity;

import simplex.entity.specification.CombinerSpecification;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.DummySpecification;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.FactorySpecification;
import simplex.entity.specification.SplitterSpecification;

/**
 * A Factory used for creating nodes.
 *
 * @author Emil
 * @author Samuel
 */
public class NodeFactory {

    private static NodeFactory nodeFactory;
    private int id = 0;

    private NodeFactory() {}

    public static NodeFactory instance() {
        if (nodeFactory == null) {
            nodeFactory = new NodeFactory();
        }
        return nodeFactory;
    }

    /**
     * Create a Factory Node.
     * @return A Node with FactorySpecification.
     */
    public Node createFactoryNode() {
        return new Node(new FactorySpecification());
    }

    /**
     * Create a Dummy Node.
     * @return A Node with DummySpecification.
     */
    public Node createDummyNode() {
        return new Node(new DummySpecification());
    }

    /**
     * Create a Consumer Node.
     * @return A Node with ConsumerSpecification.
     */
    public Node createConsumerNode() {
        return new Node(new ConsumerSpecification());
    }

    /**
     * Create a Eater Node.
     * @return A Node with EaterSpecification.
     */
    public Node createEaterNode() {
        return new Node(new EaterSpecification());
    }

    /**
     * Create a Splitter Node.
     * @return A Node with SplitterSpecification.
     */
    public Node createSplitterNode() {
        return new Node(new SplitterSpecification());
    }
    
    /**
     * Create a Combiner Node.
     * @return A Node with CombinerSpecification.
     */
    public Node createCombinerNode() {
        return new Node(new CombinerSpecification());
    }

    /**
     * Create a new id.
     * @return A new unique Id.
     */
    public int getNewId() {
        return id++;
    }

}