package simplex.entity;

import simplex.entity.specification.CombinerSpecification;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.DummySpecification;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.FactorySpecification;
import simplex.entity.specification.SplitterSpecification;

/**
 *
 * @author Emil
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

    public Node createFactoryNode() {
        return new Node(new FactorySpecification());
    }

    public Node createDummyNode() {
        return new Node(new DummySpecification());
    }

    public Node createConsumerNode() {
        return new Node(new ConsumerSpecification());
    }

    public Node createEaterNode() {
        return new Node(new EaterSpecification());
    }

    public Node createSplitterNode() {
        return new Node(new SplitterSpecification());
    }
    
    public Node createCombinerNode() {
        return new Node(new CombinerSpecification());
    }

    public int getNewId() {
        return id++;
    }

}