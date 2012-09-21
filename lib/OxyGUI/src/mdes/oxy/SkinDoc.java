/*
 * SkinDoc.java
 *
 * Created on March 22, 2008, 1:48 PM
 */

package mdes.oxy;

import java.io.InputStream;
import java.util.StringTokenizer;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.SpriteSheetFont;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author davedes
 */
public class SkinDoc extends Doc {
    
    public static final String DEFAULT_SUFFIX = "$DEFAULTSKIN";
    private Image defaultSheet;
    private Font defaultFont;
    private String title;
    private SkinDoc parent;
        
    protected SkinDoc(SkinDoc parent, Element root) throws OxyException {
        this.parent = parent;
                
        if (!"subSkin".equals(root.getNodeName())) {
            throw new OxyException("inline skin document must begin with <subSkin> tag");
        }
        
        parseRoot(root);
    }
    
    public SkinDoc(String ref) throws OxyException {
        this(ResourceLoader.getResourceAsStream(ref));
    }
    
    /**
     * Creates a new instance of SkinDoc
     */
    public SkinDoc(InputStream input) throws OxyException {        
        Document doc = readDocument(input);
        Element root = doc.getDocumentElement();
        
        if (!"skin".equals(root.getNodeName())) {
            throw new OxyException("skin document must begin with <skin> tag");
        }
        
        String sheetStr;
        String defaultFontStr;
                
        this.title = root.getAttribute("title");
        sheetStr = root.getAttribute("sheet");
        if (sheetStr==null||sheetStr.length()==0)
            throw new OxyException("must declare sheet attribute in <skin> tag");
        defaultFontStr = root.getAttribute("font");   
        
        parseRoot(root);
        
        Object sheetEl = getElement(sheetStr);
        if (sheetEl==null || !(sheetEl instanceof Image))
            throw new OxyException("sheet must point to an image element");
        else
            this.defaultSheet = (Image)sheetEl;

        if (defaultFontStr!=null&&defaultFontStr.length()!=0) {
            Object fontEl = this.getElement(defaultFontStr);
            if (fontEl==null || !(fontEl instanceof Font))
                throw new OxyException("defaultFont must point to font element");
            else
                this.defaultFont = (Font)fontEl;
        }
    }
    
    private void parseRoot(Element root) throws OxyException {
        NodeList nl = root.getChildNodes();
        for (int i=0; i<nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element e = (Element)n;
                String name = e.getNodeName();
                String varName = e.getAttribute("name");
                Object elem = null;
                if ("image".equals(name)) {
                    elem = parseImage(this, e);
                } else if ("font".equals(name)) {
                    Font f = parseFont(e);
                    if (defaultFont==null)
                        defaultFont = f;
                    elem = f;
                } else if ("effects".equals(name)) {
                    //TODO: animation options
                } else if (isComponent(name)) {
                    elem = parseComponentTheme(this, e);
                    
                    //use default key
                    if (varName==null||varName.length()==0) {
                        //custom component default:
                        //class + _default
                        //eg: "test.MiniMap$DEFAULTSKIN"
                        //    => "test_MiniMap$DEFAULTSKIN"
                        //eg: "slider"
                        //    => "slider.horizontal$DEFAULTSKIN"
                        if (name.equals("custom")) {
                            varName = e.getAttribute("class").replace('.', '_')+DEFAULT_SUFFIX;
                        } else
                            varName = name+DEFAULT_SUFFIX;
                    }
                }
                if (elem!=null && varName!=null && varName.length()!=0) {
                    if (!Doc.isIdentifier(varName))
                        throw new OxyException("invalid identifier '"+varName+"'");
                    putElement(varName, elem);
                }
            }
        }
    }
    
    public Image getDefaultSheet() {
        return parent!=null ? parent.getDefaultSheet() : defaultSheet;
    }
    
    public Font getDefaultFont() {
        return parent!=null ? parent.getDefaultFont() : defaultFont;
    }
        
    public String getTitle() {
        return parent!=null ? parent.getTitle() : title;
    }
    
    public Object getElement(String name) {
        Object obj = super.getElement(name);
        if (this.parent!=null && obj==null)
            obj = parent.getElement(name);
        return obj;            
    }
    
    public Object getSkinElement(String name) {
        return getElement(name);
    }
    
    public static ComponentTheme getDefaultTheme(Doc doc, String klass) {
        String skin = klass.replace('.', '_')+SkinDoc.DEFAULT_SUFFIX;
        
        Object obj = doc.getSkinElement(skin);
        if (obj instanceof ComponentTheme)
            return (ComponentTheme)obj;
        else
            return null;
    }
        
    public static Image parseImage(Doc doc, Element e) throws OxyException {
        String name = e.getAttribute("name");
        String type = e.getAttribute("type");
        Image i = null;
        try {
            int filter = Image.FILTER_LINEAR;
            
            String fstr = e.getAttribute("filter");
            if (fstr!=null && fstr.length()!=0)
                if (fstr.equals("NEAREST"))
                    filter = Image.FILTER_NEAREST;
            String ref = e.getAttribute("ref");
            
            if ("BigImage".equals(type)) {
                try {
                    int ts = Integer.parseInt(e.getAttribute("tileSize"));
                    i = new BigImage(ref, filter, ts);
                } catch (NumberFormatException nfe) {
                    i = new BigImage(ref, filter);
                }
            } else if ("SubImage".equals(type)) {
                Object obj = doc.getSkinElement(e.getAttribute("parent"));
                if (obj==null || !(obj instanceof Image))
                    throw new OxyException("SubImage parent must point to Image");
                Image parent = (Image)obj;
                
                try {
                    Object[] oa = Doc.parseParameters(doc, null, e.getAttribute("rect"));
                    int x = ((Integer)oa[0]).intValue();
                    int y = ((Integer)oa[1]).intValue();
                    int w = ((Integer)oa[2]).intValue();
                    int h = ((Integer)oa[3]).intValue();
                    i = parent.getSubImage(x, y, w, h);
                } catch (ClassCastException exc) {
                    throw new OxyException("all 4 values for SubImage 'rect' must be integers");
                }
            } else {
                boolean flipped = false;
                if ("true".equals(e.getAttribute("flipped")))
                    flipped = true;
                Color col = parseColor(e.getAttribute("color"));
                i = new Image(ref, flipped, filter, col);
            }
        } catch (OxyException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new OxyException("cannot create font: "+exc.getMessage());
        }
        return i;
    }
    
    private static int parseStyle(String text) {
        if ("BOLD".equals(text))
            return java.awt.Font.BOLD;
        else if ("ITALIC".equals(text))
            return java.awt.Font.ITALIC;
        else if ("PLAIN".equals(text))
            return java.awt.Font.PLAIN;
        else
            throw new IllegalArgumentException("cannot parse font style '"+text+"'");
    }
        
    public static Font parseFont(Element e) throws OxyException {
        String name = e.getAttribute("name");
        String type = e.getAttribute("type");
        Font f = null;
        try {
            if ("TrueTypeFont".equals(type)) {
                //<font name="small" type="TrueTypeFont" fontName="Verdana" 
                //      style="BOLD|ITALIC" size="10" antiAlias="true" />
                
                String fontName = e.getAttribute("fontName");
                boolean antiAlias = true;
                if ("false".equals(e.getAttribute("antiAlias")))
                    antiAlias = false;
                int size = Integer.parseInt(e.getAttribute("size"));
                
                int style = 0;
                String stylestr = e.getAttribute("style");
                if (stylestr!=null&&stylestr.length()!=0) {
                    StringTokenizer tk = new StringTokenizer(stylestr, "|");
                    while (tk.hasMoreTokens()) {
                        int next = parseStyle(tk.nextToken());
                        style |= next;
                    }
                }
                f = new TrueTypeFont(new java.awt.Font(fontName, style, size), antiAlias);
            } else if ("SpriteSheetFont".equals(type)) {
                //<font name="small" 
                //      type="SpriteSheetFont" char="A" ref="test.png" 
                //      tw="5" th="5" color="#FFFFFF" />
                String ref = e.getAttribute("ref");
                String colstr = e.getAttribute("color");
                String charstr = e.getAttribute("char");
                
                int tw = Integer.parseInt(e.getAttribute("tw"));
                int th = Integer.parseInt(e.getAttribute("th"));
                int spacing = 0;
                try {
                    spacing = Integer.parseInt(e.getAttribute("spacing"));
                } catch (Exception exc2) {}
                
                if (charstr==null||charstr.length()!=1)
                    throw new OxyException("attribute 'char' must be 1 character long");
                char startingCharacter = charstr.charAt(0);
                
                Color col = parseColor(colstr);
                SpriteSheet ss = new SpriteSheet(ref, tw, th, col, spacing);
                f = new SpriteSheetFont(ss, startingCharacter);
            } else { //AngelCodeFont
                //<font name="small" type="AngelCodeFont" fnt="test.fnt" img="test.png" />
                String fnt = e.getAttribute("fnt");
                String img = e.getAttribute("img");                
                String caching = e.getAttribute("caching");
                boolean cache = true;
                if ("false".equals(caching))
                    cache = false;
                f = new AngelCodeFont(fnt, img, cache);
            }
        } catch (OxyException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new OxyException("cannot create font: "+exc.getMessage());
        }
        return f;
    }
    
    private static int toAlign(String align) {
        String str = align.toLowerCase().intern();
        if (str=="top") return RenderState.TOP;
        else if (str=="left") return RenderState.LEFT;
        else if (str=="bottom") return RenderState.BOTTOM;
        else if (str=="right") return RenderState.RIGHT;
        else if (str=="center") return RenderState.CENTER;
        else throw new IllegalArgumentException("cannot parse align "+align);
    }
    
    private static int parseAlign(String str) {
        if (str==null||str.length()==0)
            return RenderState.CENTER;
        StringTokenizer tk = new StringTokenizer(str);
        int first = toAlign(tk.nextToken());
        int second = 0;

        if (tk.hasMoreTokens()) { //used by corners which have two tokens
            second = toAlign(tk.nextToken());
            if (second==first || second==RenderState.CENTER)
                first = 0;
        }
        return (first|second);
    }
    
    private static RenderPart parsePart(String useImageStr, String str) {
        boolean useImage = true;
        if ("false".equals(useImageStr))
            useImage = false;
        StringTokenizer tk = new StringTokenizer(str);
        int x1 = Integer.parseInt(tk.nextToken());
        int y1 = Integer.parseInt(tk.nextToken());
        int x2 = Integer.parseInt(tk.nextToken());
        int y2 = Integer.parseInt(tk.nextToken());
        return new RenderPart(useImage, x1, y1, x2, y2);
    }
        
    public static RenderState parseState(Doc doc, Element e, ComponentTheme theme) throws OxyException {
        String type = e.getAttribute("type");
        if (type==null||type.length()==0)
            type = RenderState.DEFAULT_TYPE;

        String ref = e.getAttribute("ref");
        if (ref!=null && ref.length()!=0) {
            if (ref.equals(type))
                throw new OxyException("<state> ref cannot equal type!");
            RenderState st = theme.getState(ref);
            if (st==null) {
                throw new OxyException("cannot find state ref, make sure it was" +
                        "declared BEFORE this state");
            }
            return st.copy(type);
        } else {
            RenderState state = new RenderState(type);
            String shstr = e.getAttribute("sheet");
            if (shstr!=null && shstr.length()!=0) {
                Object o = doc.getSkinElement(shstr);
                if (o!=null && !(o instanceof Image))
                    throw new OxyException("sheet must point to valid Image object");
                state.sheet = (Image)o;
            }
                
            
            String areaStr = e.getAttribute("area");
            String cornerStr = e.getAttribute("corner");
            try {
                if (areaStr!=null&&areaStr.length()!=0) { //simple create
                    RenderPart part = parsePart(e.getAttribute("useImage"), areaStr);

                    if (cornerStr!=null&&cornerStr.length()!=0) { //has border
                        float corner = Float.parseFloat(cornerStr);
                        state.simple(part.x1, part.y1, part.x2, part.y2, corner);
                    } else { //use center
                        state.put(RenderState.CENTER, part);
                    }
                } else {
                    NodeList nl = e.getElementsByTagName("subtexture");
                    for (int i=0; i<nl.getLength(); i++) {
                        Element child = (Element)nl.item(i);
                        int align = parseAlign(child.getAttribute("align"));
                        RenderPart part = parsePart(child.getAttribute("useImage"), 
                                child.getAttribute("area"));
                        state.put(align, part);
                    }
                }
            } catch (Exception exc) {
                throw new OxyException("cannot parse component RenderState: "+exc.getMessage());
            }
            state.trimParts();
            return state;
        }
    }
    
    public static void parseDefaults(Doc doc, ComponentTheme ui, Element e) throws OxyException {
        NamedNodeMap nm = e.getAttributes();
        for (int i=0; i<nm.getLength(); i++) {
            Node n = nm.item(i);
            String bean = n.getNodeName();
            String value = n.getNodeValue();
            ui.addDefault(new ComponentTheme.BeanDefault(doc, bean, value));
        }
    }
}
