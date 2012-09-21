/*
 * DefaultFactory.java
 *
 * Created on March 23, 2008, 8:50 PM
 */

package mdes.oxy;

import java.lang.reflect.InvocationTargetException;
import org.w3c.dom.Element;

/**
 * 
 * @author davedes
 */
public class DefaultFactory extends StandardFactory {
        
    protected Component createComponent(OxyDoc doc, String klass, Element e) throws OxyException {
        Desktop d = doc.getDesktop();
        
        try {
            Class c = Class.forName(klass);
            return (Component)c.getConstructor(new Class[] { Desktop.class }).newInstance(new Object[] { d });
        } catch (NoSuchMethodException ex) {
            throw new OxyException("DefaultFactory could not find the Desktop-param constructor in "+klass);
        } catch (Exception ex) {
            throw new OxyException("could not create the component from "+klass);
        }
    }    
    
    public String toThemeName(String klass, Element e) throws OxyException {
        return klass.replace('.', '_');
    }
}
