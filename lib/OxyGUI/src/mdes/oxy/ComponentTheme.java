/*
 * ComponentUI.java
 *
 * Created on February 28, 2008, 7:56 PM
 */

package mdes.oxy;

import java.lang.reflect.*;
import org.newdawn.slick.Image;
import java.util.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author davedes
 */
public class ComponentTheme {
        
    private ArrayList defaults;
    private ArrayList states;
    private ArrayList children;
    
    private RenderState defaultState;
    private SkinDoc skin;
    private ComponentTheme parent;
    private String name;
   
    
    /**
     * Creates a new instance of ComponentUI
     */
    public ComponentTheme(SkinDoc skin, ComponentTheme parent, String name) {
        this.skin = skin;
        this.name = name;
        this.parent = parent;
        if (parent!=null)
            this.parent.addChild(this);
    }
    
    public ComponentTheme(SkinDoc skin) {
        this(skin, null, null);
    }
    
    public ComponentTheme getParent() {
        return parent;
    }
    
    public String getName() {
        return name;
    }
    
    public ComponentTheme copy() {
        ComponentTheme t = new ComponentTheme(skin, parent, name);
        t.defaultState = this.defaultState;
        if (this.defaults!=null)
            t.defaults = (ArrayList)this.defaults.clone();
        if (this.states!=null)
            t.states = (ArrayList)this.states.clone();
        if (this.children!=null)
            t.children = (ArrayList)this.children.clone();
        return t;
    }
    
    void addChild(ComponentTheme theme) {
        if (children==null)
            children = new ArrayList();
        
        //remove child first if it exists
        for (int i=0; i<children.size(); i++) {
            ComponentTheme t = (ComponentTheme)children.get(i);
            if (theme.name.equals(t.name)) {
                children.set(i, theme);
                return;
            }
        }
        
        //if it wasn't found, we will add it at the end
        children.add(theme);
    }
        
    public ComponentTheme getChild(String name) {
        if (children==null)
            return null;
        for (int i=0; i<children.size(); i++) {
            ComponentTheme t = (ComponentTheme)children.get(i);
            if (name.equals(t.name))
                return t;
        }
        return null;
    }
        
    public void addState(RenderState state) {
        if (states==null)
            states = new ArrayList();
        if (defaultState==null && RenderState.DEFAULT_TYPE.equals(state.getType()))
            defaultState = state;
        
        //remove state first if it exists
        for (int i=0; i<states.size(); i++) {
            RenderState st = (RenderState)states.get(i);
            if (state.type.equals(st.getType())) {
                states.set(i, state);
                return;
            }
        }
        
        states.add(state);
    }
    
    public RenderState getState(String type) {
        if (states==null)
            return null;
        for (int i=0; i<states.size(); i++) {
            RenderState st = (RenderState)states.get(i);
            if (type.equals(st.getType()))
                return st;
        }
        return null;
    }
           
    public void addDefault(Default def) {
        if (defaults==null)
            defaults = new ArrayList();
        defaults.add(def);
    }
    
    public void removeDefault(Default def) {
        if (defaults!=null)
            defaults.remove(def);
    }
    
    protected void trim() {
        if (defaults!=null)
            defaults.trimToSize();
        if (states!=null)
            states.trimToSize();
    }
    
    public void initComponent(Component comp) throws OxyException {
        if (defaults!=null) {
            for (int i=0; i<defaults.size(); i++) {
                Default def = (Default)defaults.get(i);
                def.setup(comp);
            }
        }
    }
    
    public boolean contains(Component comp, float x, float y) {
        return comp.inside(x, y);
    }
    
    public void setDefaultState(RenderState state) {
        this.defaultState = state;
    }
    
    public RenderState getDefaultState() {
        return defaultState;
    }
    
    public void render(Component comp) {
        render(comp, defaultState);
    }
    
    public void render(Component comp, RenderState state) {
        render(comp, state, comp.alphaFilter);
    }
    
    public void render(Component comp, RenderState state, Color filter) {
        Rectangle r = comp.getBoundsOnScreen();
        render(comp, state, r.getX(), r.getY(), r.getWidth(), r.getHeight(), filter);
    }
    
    public void render(Component comp, RenderState state, float x, float y, float width, float height, Color filter) {
        if (state==null) {
            if (defaultState==null)
                return;
            else
                state = defaultState;
        }
            
        Image sheet = state.sheet!=null ? state.sheet : comp.getDesktop().getSheet();
        if (sheet==null)
            return;
        sheet.startUse();
        filter.bind();
        state.render(sheet, x, y, width, height, filter);
        sheet.endUse();
    }
    
    public static interface Default {
        public void setup(Component e) throws OxyException;
    }
        
    public static class BeanDefault implements Default {
        public final String bean;
        public final String value;
        public final Doc doc;
        
        BeanDefault(Doc doc, String bean, String value) {
            this.doc =  doc;
            this.bean = bean;
            this.value = value;
        }
        
        public void setup(Component comp) throws OxyException {
            Doc.setBean(doc, comp, bean, value);
        }
    }
}