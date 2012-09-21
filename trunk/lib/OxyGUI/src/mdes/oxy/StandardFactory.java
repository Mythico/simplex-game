/*
 * StandardFactory.java
 *
 * Created on March 22, 2008, 1:28 PM
 */

package mdes.oxy;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * StandardFactory contains all the standard definitions for non-custom components.
 * @author davedes
 */
class StandardFactory implements ComponentFactory {
    
    public Component parseComponent(OxyDoc doc, String klass, Element e) throws OxyException {
        klass = klass.intern();
        
        //create a new instance
        Component comp = createComponent(doc, klass, e);
        
        //parses the component (setters, etc)
        parseComponent(doc, klass, e, comp);
        
        return comp;
    }
    
    /**
     * Creates a new instance of the component with the given details.
     */
    protected Component createComponent(OxyDoc doc, String klass, Element e) throws OxyException {
        Desktop d = doc.getDesktop();
               
        if (klass == "desktop") {
            Desktop desktop = new Desktop(doc.getClient(), doc.getContext(), doc);
            doc.desktop = desktop;
            return desktop;
        } else if (klass == "component") {
            return new Component(d);
        } else if (klass == "label") {
            return new Label(d);
        } else if (klass == "button") {
            return new Button(d);
        } else if (klass == "panel") {
            return new Panel(d);
        } else if (klass == "window") {
            return new Window(d);
        } else if (klass == "dialog") {
            return new Dialog(d);
        } else if (klass == "textField") {
            return new TextField(d);
        } else if (klass == "hSlider") {
            return new Slider(d, Slider.HORIZONTAL);
        } else if (klass == "vSlider") {
            return new Slider(d, Slider.VERTICAL);
        } else if (klass == "vScroll") {
            return new ScrollBar(d, ScrollBar.VERTICAL);
        } else if (klass == "hScroll") {
            return new ScrollBar(d, ScrollBar.HORIZONTAL);
        } else if (klass == "spinner") {
            return new Spinner(d);
        } else if (klass == "toolTip") {
            return new ToolTip(d);
        } else {
            throw new OxyException("StandardFactory cannot recognize component "+klass);
        }
    }
        
    protected void parseComponent(OxyDoc doc, String klass, Element e, Component comp) throws OxyException {
        String skinStr = e.getAttribute("theme");
        ComponentTheme theme;
        if (skinStr==null || skinStr.length()==0) {
            theme = SkinDoc.getDefaultTheme(doc, klass);
        } else {
            Object obj = doc.getSkinElement(skinStr);
            if (obj==null || !(obj instanceof ComponentTheme))
                throw new OxyException("theme attribute "+skinStr+" must point to named ComponentTheme");
            theme = (ComponentTheme)obj;
        }
        comp.setTheme(theme);
        
        //parse setters
        NamedNodeMap nm = e.getAttributes();
        for (int i=0; i<nm.getLength(); i++) {
            Node n = nm.item(i);
            String bean = n.getNodeName();
            String value = n.getNodeValue();
            parseSetter(doc, comp, bean.intern(), value);
        }
        
        //parse <set> tags
        NodeList nl = e.getChildNodes();
        for (int i=0; i<nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element && "set".equals(n.getNodeName())) {
                Element child = (Element)n;
                nm = child.getAttributes();
                for (int x=0; x<nm.getLength(); x++) {
                    Node n2 = nm.item(x);
                    String bean = n2.getNodeName();
                    String value = n2.getNodeValue();
                    parseSetter(doc, comp, bean.intern(), value);
                }
            }
        }
        
        comp.onPostParse(doc);
    }
    
    /**
     * 
     * @param bean the bean attribute after a call to intern()
     */
    protected void parseSetter(OxyDoc doc, Component comp, String bean, String value) throws OxyException {
        Doc.setBean(doc, comp, bean, value);
    }
    
    public ComponentTheme parseComponentTheme(SkinDoc doc, String klass, Element e) throws OxyException {
        String refStr = e.getAttribute("ref");
        ComponentTheme ref = null;
        if (refStr!=null && refStr.length()!=0) {
            if (refStr.equals("default"))
                ref = SkinDoc.getDefaultTheme(doc, klass);
            else {
                Object obj = doc.getElement(refStr);
                if (!(obj instanceof ComponentTheme))
                    throw new OxyException("theme ref must point to another component theme");
                ref = (ComponentTheme)obj;
            }
        }
        ComponentTheme theme = ref!=null ? ref.copy() : new ComponentTheme(doc);
        parseComponentTheme(doc, klass, e, theme);
        return theme;
    }
    
    /**
     * The returned component theme will put put in the skin doc (as it should be named).
     */
    private void parseComponentTheme(SkinDoc doc, String klass, Element e, ComponentTheme theme) throws OxyException {
        NodeList nl = e.getChildNodes();
        Element controls = null;
        for (int i=0; i<nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element child = (Element)n;
                String nodeName = child.getNodeName();
                if ("defaults".equals(nodeName)) {
                    SkinDoc.parseDefaults(doc, theme, child);
                } else if ("state".equals(nodeName)) {
                    RenderState state = SkinDoc.parseState(doc, child, theme);
                    theme.addState(state);
                } else if ("controls".equals(nodeName)) {
                    if (controls!=null)
                        throw new OxyException("can only have one <controls> tag per component theme");
                    controls = child;
                }
            }
        }
        
        if (controls!=null) {
            nl = controls.getChildNodes();
            for (int i=0; i<nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n instanceof Element) {
                    ComponentTheme child = new ComponentTheme(doc, theme, n.getNodeName());
                    parseComponentTheme(doc, klass, (Element)n, child);
                }
            }
        }
    }
}
