/*
 * ComponentFactory.java
 *
 * Created on March 22, 2008, 1:24 PM
 */

package mdes.oxy;

import org.w3c.dom.Element;

/**
 *
 * @author davedes
 */
public interface ComponentFactory {
    
    public Component parseComponent(OxyDoc doc, String klass, Element e) throws OxyException;
    public ComponentTheme parseComponentTheme(SkinDoc doc, String klass, Element e) throws OxyException;
}
