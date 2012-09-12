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
 * @author Emil, Samuel
 */
public class ImageManager {

    public static Image dummy_node;
    public static Image factory_node;
    public static Image white_resource;
    public static Image red_resource;
    public static Image green_resource;
    public static Image blue_resource;

    public static void init() throws SlickException {
        dummy_node = new Image("img/dummy_node.png", Color.white);
        factory_node = new Image("img/factory_node.png", Color.white);
        white_resource = new Image("img/white_resource.png", Color.white);
        red_resource = new Image("img/red_resource.png", Color.white);
        green_resource = new Image("img/green_resource.png", Color.white);
        blue_resource = new Image("img/blue_resource.png", Color.white);
    }

}
