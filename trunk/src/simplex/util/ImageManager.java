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
    public static Image consumer_node;
    public static Image happy_consumer_node;
    
    public static Image connection_icon;
    
    public static Image white_resource;
    public static Image red_resource;
    public static Image green_resource;
    public static Image blue_resource;
    
    public static Image sidebar;

    public static void init() throws SlickException {
        dummy_node = new Image("img/dummy_node.png", Color.white);
        factory_node = new Image("img/factory_node.png", Color.white);
        consumer_node = new Image("img/consumer_node.png", Color.white);
        happy_consumer_node = new Image("img/happy_consumer_node.png", Color.white);
        
        connection_icon = new Image("img/connection.png", Color.white);
        
        white_resource = new Image("img/white_resource.png");
        red_resource = new Image("img/red_resource.png");
        green_resource = new Image("img/green_resource.png");
        blue_resource = new Image("img/blue_resource.png");
        
        sidebar = new Image("img/sidebar.png");
    }

}
