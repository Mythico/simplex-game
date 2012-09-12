/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import simplex.util.GridConversions;
import simplex.util.ImageManager;

/**
 * 
 * @author Emil
 */
public class NodeFactory {

    public Node createFactory(int x, int y, int resourceType, int resourceRate) {
        Node node = new Node(GridConversions.gridToScreenCoord(x, y));
        node.setResourceRate(resourceRate);
        node.setResourceType(resourceType);
        node.setImage(ImageManager.factory_node);
        return node;
    }

    public Node createDummyNode(int x, int y) {
        Node node = new Node(GridConversions.gridToScreenCoord(x, y));
        node.setImage(ImageManager.dummy_node);
        return node;
    }
}