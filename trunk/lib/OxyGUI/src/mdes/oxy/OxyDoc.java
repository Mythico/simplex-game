/*
 * OxyDoc.java
 *
 * Created on March 22, 2008, 3:38 PM
 */

package mdes.oxy;

import java.io.IOException;
import java.io.InputStream;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author davedes
 */
public class OxyDoc extends Doc {
    
    Desktop desktop;
    private GUIContext context;
    private SkinDoc subSkin;
    private Object client;
    
    /**
     * Creates a new instance of OxyDoc
     */
    public OxyDoc(Object client, GUIContext context, InputStream input) throws OxyException {
        this(client, context, new InputSource(input));
        try { input.close(); }
        catch (IOException e) { throw new OxyException("cannot close input stream"); }
    }
    
    /**
     * Creates a new instance of OxyDoc
     */
    public OxyDoc(Object client, GUIContext context, InputSource input) throws OxyException {
        SkinDoc parentSkin = Desktop.getSkin();
        if (parentSkin == null)
            throw new OxyException("no parent skin is set!");
        Document document = readDocument(input);
        Element root = document.getDocumentElement();
        this.context = context;
        this.client = client;
        
        if (context==null)
            throw new IllegalArgumentException("context cannot be null");
        if (client==null)
            throw new IllegalArgumentException("client cannot be null");
        
        NodeList skins = root.getElementsByTagName("subSkin");
        int len = skins.getLength();
        if (len>1) {
            throw new OxyException("can only have one subSkin per component document");
        } else if (len==1) {
            Element subSkinEl = (Element)skins.item(0);
            subSkin = new SkinDoc(parentSkin, subSkinEl);
        }
        
        parseComponentTree(root, null);
        resolveReferences(desktop);
    }
    
    public OxyDoc(Object client, GUIContext context, String ref) throws OxyException {
        this(client, context, ResourceLoader.getResourceAsStream(ref));
    }
    
    public Object getSkinElement(String name) {
        Object obj = null;
        if (subSkin!=null)
            return subSkin.getElement(name);
        else
            return Desktop.getSkin().getElement(name);
    }
        
    public Object getClient() {
        return client;
    }
    
    public GUIContext getContext() {
        return context;
    }
    
    public Desktop getDesktop() {
        return desktop;
    }
    
    public void resolveReferences(Component comp) throws OxyException {
        comp.resolveReferences(this);
        if (comp instanceof Panel) {
            Panel p = (Panel)comp;
            for (int i=0; i<p.getChildCount(); i++) {
                resolveReferences(p.getChild(i));
            }
        }
    }
        
    public Component parseComponentTree(Element e, Panel parent) throws OxyException {
        Component comp = parseComponent(this, e);
        String name = e.getAttribute("name");
        if (name!=null&&name.length()!=0) {
            if (!Doc.isIdentifier(name))
                throw new OxyException("invalid identifier '"+name+"'");
            if (getElement(name)!=null)
                throw new OxyException("an element already exists with the name '"+name+"'");
            putElement(name, comp);
        }
        
        if (parent!=null && comp instanceof Desktop) {
            throw new OxyException("<desktop> can only exist at the root of a document");
        }
        
        if (comp instanceof Panel) {
            Panel p = (Panel)comp;
            NodeList nl = e.getChildNodes();
            for (int i=0; i<nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n instanceof Element) {
                    Element child = (Element)n;
                    if (isComponent(child.getNodeName())) {
                        Component childComp = parseComponentTree(child, p);
                        p.add(childComp);
                    }
                }
            }
        }
        
        return comp;
    }
}
