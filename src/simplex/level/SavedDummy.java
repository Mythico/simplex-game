/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.level;

import simplex.entity.Node;
import simplex.entity.NodeFactory;
import simplex.util.GridCoord;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class SavedDummy extends SavedNode {

    private SavedDummy() {
    }

    public SavedDummy(int x, int y, int id) {
        super(x, y, id);
    }

    @Override
    public Node create() {
        Node node = NodeFactory.instance().createDummyNode();
        node.setGridPosition(new GridCoord(getX(), getY()));
        return node;
    }
}
