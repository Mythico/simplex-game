/*
 * Panel.java
 *
 * Created on February 28, 2008, 10:35 PM
 */

package mdes.oxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.newdawn.slick.Graphics;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author davedes
 */
public class Panel extends Component {
    
    ArrayList children = new ArrayList();
    boolean childrenDirty = false;
    private ZComparator zCompare = new ZComparator();
    
    public Panel(Desktop desktop) {
        super(desktop);
    }
    
    Panel() {
        super();
    }
        
    /**
     * Called to ensure the z-ordering of
     * this panel's children is correct.
     * If it isn't, it will be sorted appropriately.
     */
    public void ensureZOrder() {
        if (childrenDirty) {
            Collections.sort(children, zCompare);
            childrenDirty = false;
        }
    }
    
    public void add(Component child) {
        if (!children.contains(child)) {
            childrenDirty = true;
            child.parent = this;
            children.add(child);
        }
    }
        
    public boolean remove(Component child) {
        boolean contained = children.remove(child);
        if (contained) {
            childrenDirty = true;
            child.parent = null;
        }
        return contained;
    }
    
    public int getChildCount() {
        return children.size();
    }
    
    public Component getChild(int index) {
        ensureZOrder();
        return (Component)children.get(index);
    }
    
    /**
     * Called to recursively render all children of this container.
     *
     * @param container the GUIContext we are rendering to
     * @param g the Graphics context we are rendering with
     */
    protected void renderChildren(Graphics g) {
        for (int i=0; i<getChildCount(); i++) {
            Component child = getChild(i);
            child.render(g);
        }
    }
    
    /**
     * Called to recursively update all children of this container.
     *
     * @param container the GUIContext we are rendering to
     * @param delta the delta time (in ms)
     */
    protected void updateChildren(int delta) {
        for (int i=0; i<getChildCount(); i++)
            getChild(i).update(delta);
    }
    
    public void update(int delta) {
        super.update(delta);
        updateChildren(delta);
    }
    
    public void render(Graphics g) {
        super.render(g);
        renderChildren(g);
    }
    
    private class ZComparator implements Comparator{
        public int compare(Object o1, Object o2) {
            Component c1 = (Component)o1;
            Component c2 = (Component)o2;
            int res;
            if (c1 == null || c2 == null || c1.equals(c2)) {
                res = 0;
            } else {
                res = (c2.getZIndex() < c1.getZIndex() ? 1 : -1);              
            }
            return res;
        }
    }
}