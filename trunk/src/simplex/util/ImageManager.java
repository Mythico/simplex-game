/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Emil
 */
public class ImageManager {
    
    public static Image dummy_node;
    public static Image factory_node;
    public static Image white_resource;
    
    
    public static void init() throws SlickException{
	dummy_node = new Image("img/dummy_node.png", Color.white);
	factory_node = new Image("img/factory_node.png", Color.white);
	white_resource = new Image("img/white_resource.png", Color.white);
    }
    
}
