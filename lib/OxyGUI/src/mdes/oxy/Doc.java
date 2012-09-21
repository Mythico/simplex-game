/*
 * Doc.java
 *
 * Created on March 22, 2008, 2:16 PM
 */

package mdes.oxy;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author davedes
 */
public abstract class Doc {
    
    protected static final String THIS_ID = "this";
    protected static final String i18n_PREFIX = "i18n";
    
    private static ResourceBundle resources = null;
    private static ArgTokenizer tk = new ArgTokenizer();
    private static StandardFactory standardFactory = new StandardFactory();
    public static final String DEFAULT_FACTORY = DefaultFactory.class.getName();
    private static HashMap factoryCache = new HashMap();
    
    private static final String[] PRIMARY_COMPONENTS = new String[] {
        "component", "label", "button", "scrollPane",
        "textArea", "textField", "toolTip", "comboBox", "popupMenu",
        "panel", "desktop", "window", "dialog", "custom",
        "hScroll", "vScroll", "hSlider", "vSlider", "spinner"
    };
    
    public static ResourceBundle getResources() {
        return resources;
    }
    
    public static void setResources(ResourceBundle aBundle) {
        resources = aBundle;
    }
    
    //<String, OxyElement>
    protected HashMap elements = new HashMap();
    
    /** Creates a new instance of Doc */
    public Doc() {
    }
    
    protected Document readDocument(InputSource in) throws OxyException {
        try { 
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(in);
        } catch (IOException e) {
            throw new OxyException("Cannot read document.");
        } catch (ParserConfigurationException e) {
            throw new OxyException("Cannot set up parser.");
        } catch (SAXException e) {
            throw new OxyException("Error parsing document -- check XML.");
        }
    }
    
    protected Document readDocument(InputStream in) throws OxyException {
        try { 
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(in);
        } catch (IOException e) {
            throw new OxyException("Cannot read document.");
        } catch (ParserConfigurationException e) {
            throw new OxyException("Cannot set up parser.");
        } catch (SAXException e) {
            throw new OxyException("Error parsing document -- check XML.");
        } finally {
            try { in.close(); }
            catch (Exception e) {}
        }
    }
    
    protected Object putElement(String name, Object obj) {
        return elements.put(name, obj);
    }
    
    public abstract Object getSkinElement(String name);
    
    public Object getElement(String name) {
        return elements.get(name);
    }
    
    public static boolean isComponent(String name) {
        for (int i=0; i<PRIMARY_COMPONENTS.length; i++) {
            if (PRIMARY_COMPONENTS[i].equals(name))
                return true;
        }
        return false;
    }
    
    
    public static void setBean(Doc doc, Component comp, String bean, String value) throws OxyException {
        bean = bean.intern();
        if (comp.handleSpecialSetter(bean, value))
            return;
        
        //test => setTest
        String methodName = Doc.toMethodName("set", bean);
        try {
            Object[] params = Doc.parseParameters(doc, comp, value);
            Class[] paramTypes = new Class[params.length];
            for (int i=0; i<paramTypes.length; i++) {
                paramTypes[i] = Doc.toPrimitive(params[i]);
            }
            Method m = comp.getClass().getMethod(methodName, paramTypes);
            m.invoke(comp, params);
        } catch (OxyException e) {
            throw e;
        } catch (Exception e) {
            throw new OxyException("can't invoke setter method "+methodName+" with params "+value);
        }
    }
    
    static Class toPrimitive(Object obj) {
        if (obj==null)
            return Object.class;
        if (obj instanceof Null) {
            return ((Null)obj).type;
        } else if (obj instanceof Percent) {
            return Position.class;
        } else if (obj instanceof Image) {
            return Image.class;
        } else if (obj instanceof Font) {
            return Font.class;
        } else {
            Class cls = obj.getClass();
            if (cls==Boolean.class) {
                return Boolean.TYPE;
            } else if (obj instanceof Number) {
                if (cls==Integer.class)
                    return Integer.TYPE;
                else if (cls==Double.class)
                    return Double.TYPE;
                else if (cls==Float.class)
                    return Float.TYPE;
                else if (cls==Long.class)
                    return Long.TYPE;
                else if (cls==Short.class)
                    return Short.TYPE;
                else if (cls==Byte.class)
                    return Byte.TYPE;
            }
            return cls;
        }
    }
    
    public static Method findMethod(Object client, String name, Object[] params) throws OxyException {
        Class[] klass = null;
        if (params!=null) {
            klass = new Class[params.length];        
            for (int i=0; i<klass.length; i++) {
                klass[i] = toPrimitive(params[i]);
                if (params[i] instanceof Null)
                    params[i] = null;
            }
        }
        try { 
            return client.getClass().getMethod(name, klass);
        } catch (Exception e) {
            throw new OxyException("cannot find method "+name+" with given params");
        }
    }
    
    public static Object invokeMethod(Method method, Object client, Object[] params) throws OxyException {
        try { 
            return method.invoke(client, params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OxyException("cannot invoke "+method.getName()+" with given params");
        }
    }
    
    public static Object[] parseParameters(Doc doc, Object thisNode, String args) throws OxyException {
        boolean deb = false;
        if (args.equals("0")) {
            deb = true;
        }
        tk.set(args);
        ArrayList bag = new ArrayList();
        while (tk.hasNext()) {
            String token = tk.next().trim();
            bag.add(parseObject(doc, thisNode, token));
        }
        return bag.toArray();
    }
        
    static String toMethodName(String prefix, String getter) {
        if (getter.length()>1)
            return prefix+Character.toUpperCase(getter.charAt(0)) + getter.substring(1);
        else
            return prefix+Character.toUpperCase(getter.charAt(0));
    }
    
    public static Object parseObject(Doc doc, Object thisNode, String token) throws OxyException {
        if (token==null)
            throw new IllegalArgumentException("cannot parse null arg");
        int len = token.length();
        if (token.length()==0) {
            throw new OxyException("cannot parse empty arg");
        } else if ( (token.charAt(0)=='\''&&token.charAt(len-1)=='\'')
                || (token.charAt(0)=='\"'&&token.charAt(len-1)=='\"') ) {
            return token.substring(1, len-1);
        } else {
            Class nullType = Object.class;
            Object obj = null;
            //parse simple types
            if (token.startsWith("null")) {
                int prelen = "null".length();
                if (token.length()==prelen) {
                    obj = new Null(Object.class);
                } else {
                    char stop = ':';
                    if (token.charAt(prelen)!=stop)
                        throw new OxyException("null delim must be '"+stop+"'");
                    String typeStr = token.substring(prelen+1);
                    try {
                        obj = new Null(Class.forName(typeStr));
                    } catch (Exception e) {
                        throw new OxyException("cannot find class "+typeStr+" for null wrapper");
                    }
                }
            } else if (token.equals("true"))
                obj = Boolean.TRUE;
            else if (token.equals("false"))
                obj = Boolean.FALSE;
            else if (token.startsWith(i18n_PREFIX)) {
                int prelen = i18n_PREFIX.length();
                ResourceBundle bundle = getResources();
                if (token.length()==prelen) {
                    obj = bundle;
                    nullType = ResourceBundle.class;
                } else {
                    char stop = '.';
                    if (token.charAt(prelen)!=stop)
                        throw new OxyException(i18n_PREFIX+" delim must be '"+stop+"'");
                    if (bundle!=null) {
                        String key = token.substring(prelen+1);
                        try {
                            obj = bundle.getString(key);
                        } catch (MissingResourceException e) {
                            obj = null;
                        }
                    } else {
                        obj = null;
                    }
                    nullType = String.class;
                }   
            } else {
                boolean propGet = false;
                
                String id = token; 
                
                //try '.' splitter
                int dot = token.indexOf('.');
                                
                //split the string
                if (dot>=0)
                    id = token.substring(0, dot);
                
                //want to parse a member
                if (isIdentifier(id)) {
                    Object member = null;
                    
                    if (id.equals(THIS_ID)) {
                       if (thisNode==null)
                            throw new OxyException("passed element 'this' is null");
                        member = thisNode;
                    } else 
                        member = doc.getElement(id);
                                        
                    if (member==null) {
                        throw new OxyException("can't find member '"+id+"'");
                    }
                    
                    if (dot<0) { //just a member
                        obj = member;
                    } else { //also has a bean getter
                        String getter = token.substring(dot+1);
                        String bean = getter;
                        //test => getTest
                        getter = toMethodName("get", bean);
                        try {
                            Method method = member.getClass().getMethod(getter, null);
                            if (method.getReturnType()==Void.TYPE)
                                throw new OxyException("getter method '"+getter+"' must return a value");
                            obj = method.invoke(member, null);
                            nullType = method.getReturnType();
                        } catch (Exception e) {
                            getter = toMethodName("is", bean);
                            try {
                                Method method = member.getClass().getMethod(getter, null);
                                if (method.getReturnType()==Void.TYPE)
                                    throw new OxyException("getter method '"+getter+"' must return a value");
                                obj = method.invoke(member, null);
                                nullType = method.getReturnType();
                            } catch (Exception exc) {
                                exc.printStackTrace();
                                throw new OxyException("can't access getter bean '"+bean+"' for member "+id);
                            }
                        }
                    }
                } else {
                    char last = token.charAt(len-1);
                    
                    if (last=='%') {
                        try {
                            float f = Float.parseFloat(token.substring(0, len-1));
                            obj = new Percent(f/100f);
                        } catch (NumberFormatException exc) {
                            throw new OxyException("can't parse percentage '"+token+"'");
                        }
                    } else if (last=='x') {
                        int i = token.indexOf("px");
                        try {
                            float f = Float.parseFloat(token.substring(0, i).trim());
                            obj = new Position(f);
                        } catch (NumberFormatException exc) {
                            throw new OxyException("can't parse position '"+token+"'");
                        }
                    } else { //normal number
                        char upper = Character.toUpperCase(last);
                        try {
                            try {
                                obj = Integer.decode(token);
                            } catch (Exception ex) {
                                if (upper=='F')
                                    obj = Float.valueOf(token);
                                else if (upper=='D')
                                    obj = Double.valueOf(token);
                                else if (upper=='L')
                                    obj = Long.valueOf(token.substring(0, len-1));
                                else if (dot >= 0)
                                    obj = Float.valueOf(token);
                                else {
                                    obj = Integer.valueOf(token); 
                                }
                            }
                        } catch (NumberFormatException e) {
                            throw new OxyException("can't parse arg "+token);
                        }
                    }
                }
            }
            if (obj==null)
                obj = new Null(nullType);
            return obj;
        }
    }
        
    public static Color parseColor(String token) throws OxyException {
        if (token==null||token.length()==0)
            return null;
        Object[] obj = parseParameters(null, null, token);
        if (obj.length==1)
            return parseColor(obj[0], token);
        else
            return parseColor(obj, token);
    }
    
    private static Color parseColor(Object obj, String token) throws OxyException {
        if (obj==null || obj instanceof Doc.Null) {
            return null;
        } else if (obj instanceof Integer) {
            Color c = new Color(((Integer)obj).intValue());
            c.a = 1f;
            return c;
        } else if (obj instanceof String) {
            Color c = Color.decode(obj.toString());
            c.a = 1f;
            return c;
        } else if (obj instanceof Object[]) {
            Object[] array = (Object[])obj;
            boolean ok = false;
            if ((array.length==3 || array.length==4) && allNumbers(array)) {
                if (hasFloatOrDouble(array)) {
                    float r = ((Number)array[0]).floatValue();
                    float g = ((Number)array[1]).floatValue();
                    float b = ((Number)array[2]).floatValue();
                    float a = 1f;
                    if (array.length==4)
                        a = ((Number)array[3]).floatValue();
                    return new Color(r, g, b, a);
                } else {
                    int r = ((Number)array[0]).intValue();
                    int g = ((Number)array[1]).intValue();
                    int b = ((Number)array[2]).intValue();
                    int a = 255;
                    if (array.length==4)
                        a = ((Number)array[3]).intValue();
                    return new Color(r, g, b, a);
                }
            } else
                throw new OxyException("must have exactly 3 or 4 valid number arguments");
        } else
            throw new OxyException(token+" is not a valid color object");
    }
    
    private static boolean allNumbers(Object[] a) {
        for (int i=0; i<a.length; i++) {
            if (!(a[i] instanceof Number))
                return false;
        }
        return true;
    }
    
    private static boolean hasFloatOrDouble(Object[] a) {
        for (int i=0; i<a.length; i++) {
            if (a[i] instanceof Float || a[i] instanceof Double)
                return true;
        }
        return false;
    } 
    
    public static boolean isIdentifier(String s) {
        if (s==null)
            return false;
        if (s.length()==0 || !Character.isJavaIdentifierStart(s.charAt(0)))
            return false;
        for (int i=1; i<s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isJavaIdentifierPart(s.charAt(i)))
                return false;
        }
        return true;
    }
        
    public static Component parseComponent(OxyDoc doc, Element e) throws OxyException {
        String node = e.getNodeName();
        if ("custom".equals(node)) {
            String klass = e.getAttribute("class");
            String factName = e.getAttribute("factory");
            return findFactory(klass, factName).parseComponent(doc, klass, e);
        } else {
            return standardFactory.parseComponent(doc, node, e);
        }
    }
    
    public static ComponentTheme parseComponentTheme(SkinDoc doc, Element e) throws OxyException {
        String node = e.getNodeName();
        String klass;
        ComponentFactory fact;
        
        if ("custom".equals(node)) {
            klass = e.getAttribute("class");
            String factName = e.getAttribute("factory");
            if (factName==null || factName.length()==0)
                factName = DEFAULT_FACTORY;
            fact = findFactory(klass, factName);
        } else {
            klass = node;
            fact = standardFactory;
        }
        return fact.parseComponentTheme(doc, klass, e);
    }
    
    private static ComponentFactory findFactory(String klass, String factName) throws OxyException {
        if (factName==null||factName.length()==0)
            factName = DEFAULT_FACTORY;
        ComponentFactory cached = (ComponentFactory)factoryCache.get(factName);
        if (cached!=null)
            return cached;
        try {
            ComponentFactory f = (ComponentFactory)Class.forName(factName).newInstance();
            factoryCache.put(factName, f);
            return f;
        } catch (Exception ex) {
            throw new OxyException("could not load factory named "+factName+", "+ex.getMessage());
        }
    }
    
    public static class Null {
        Class type;
        Null(Class type) {
            this.type = type;
        }
        
        public Class getType() {
            return type;
        }
    }
     
    private static class ArgTokenizer {
        private String str;
        private int pointer=0, len=0;
        
        public ArgTokenizer() {
        }
        
        public void set(String str) {
            this.str = str;
            pointer = 0;
            len = str.length();
        }
        
        public boolean hasNext() {
            if (str==null)
                return false;
            return pointer <= len-1; 
        }
                
        public String next() {
            final int SINGLE = 1;
            final int DOUBLE = 2;
            int quotes = 0;
            
            int start = pointer;
            int end = len;
            int tokStart = start;
            boolean hit = false;
            
            
            //single: 'Let\'s call it a "test," ok?'  , 0, 5, TEST
            //double: "Let's call it a \"test,\" ok?"  , 0, 5, TEST
            for (int i=start; i<len; i++) {
                char c = str.charAt(i);
                
                //if we hit non-whitespace
                if (!hit)
                    hit = !Character.isWhitespace(c);
                                
                if (!hit) { //we have not hit anything
                    tokStart++;
                } else { //we have hit something
                    if (i==tokStart) { //we are on the first char of the arg
                        if (c=='\'')
                            quotes = SINGLE;
                        else if (c=='\"')
                            quotes = DOUBLE;
                    } else { //not on first char
                        if (quotes!=0) { //if we are currently in quotes
                            //if we have hit a non-escaped quote (end)
                            if ( ((quotes==SINGLE && c=='\'') || (quotes==DOUBLE && c=='\"')) ) {
                                if (str.charAt(i-1)=='\\') {
                                    str = str.substring(0, i-1) + str.substring(i);
                                    len = str.length();
                                    i--;
                                } else {
                                    end = i+1;
                                    break;
                                }
                            }
                        } else { //not in quotes
                            if (c==',') {
                                end = i;
                                break;
                            }
                        }
                    }
                }
            }
            pointer = end;
            //move pointer up to next ',' if there is one
            for ( ; pointer<len; pointer++) {
                if (str.charAt(pointer)==',') {
                    pointer++;
                    break;
                }
            }
            return str.substring(start, end);
        }
    }
}
