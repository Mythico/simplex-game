/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.FactorySpecification;
import simplex.entity.specification.NodeSpecification;
import simplex.entity.Resource;
import simplex.entity.specification.SplitterSpecification;

/**
 *
 * @author Emil, Samuel
 */
public class ImageManager {

    private static Image dummy_node;
    private static Image factory_node;
    private static Image consumer_node;
    private static Image happy_consumer_node;
    private static Image eater_node;
    private static Image splitter_node;
    private static Image connection_icon;
    public static Image connection_swap_icon;
    private static Image white_resource;
    private static Image red_resource;
    private static Image green_resource;
    private static Image blue_resource;
    private static Map<NodeSpecification, Map<Resource, Image>> nodeResourceMap;

    public static void init() throws SlickException {
        dummy_node = new Image("img/dummy_node.png", Color.white);
        factory_node = new Image("img/factory_node.png", Color.white);
        consumer_node = new Image("img/consumer_node.png", Color.white);
        happy_consumer_node = new Image("img/happy_consumer_node.png", Color.white);
        eater_node = new Image("img/dummy_node.png", Color.white);
        splitter_node = new Image("img/dummy_node.png", Color.white);

        connection_icon = new Image("img/connection.png", Color.white);
        connection_swap_icon = new Image("img/connection_swap.png", Color.white);

        white_resource = new Image("img/white_resource.png");
        red_resource = new Image("img/red_resource.png");
        green_resource = new Image("img/green_resource.png");
        blue_resource = new Image("img/blue_resource.png");

        nodeResourceMap = new HashMap<>();
    }

    public static Image get(Resource resource) {
        switch (resource.getType()) {
            case Resource.RED:
                return red_resource;
            case Resource.BLUE:
                return blue_resource;
            case Resource.GREEN:
                return green_resource;
            default:
                return white_resource;
        }
    }

    public static Image get(NodeSpecification spec) {
        Map<Resource, Image> resourceMap = nodeResourceMap.get(spec);

        if (resourceMap == null) {
            resourceMap = new HashMap<>();
            nodeResourceMap.put(spec, resourceMap);
        }
        final Resource r = spec.getResource();
        Image img = resourceMap.get(r);

        if (img == null) {
            if (spec instanceof FactorySpecification) {
                img = createResourceImage(factory_node, r);
            } else if (spec instanceof ConsumerSpecification) {
                if (((ConsumerSpecification) spec).isHappy()) {
                    img = createResourceImage(happy_consumer_node, r);
                } else {
                    img = createResourceImage(consumer_node, r);
                }
            } else if (spec instanceof EaterSpecification) {
                img = createResourceImage(eater_node, r);
            } else if (spec instanceof SplitterSpecification) {
                img = createResourceImage(splitter_node, r);
            } else {
                img = createResourceImage(dummy_node, r);
            }
            resourceMap.put(r, img);
        }
        return img;
    }

    private static Image createResourceImage(Image background, Resource resource) {
        Image img = null;
        try {
            img = new Image(32, 32);
            Graphics g = img.getGraphics();
            g.drawImage(background, 0, 0, Color.white);

            Image ball = ImageManager.get(resource).getScaledCopy(8, 8);
            int baseX = img.getWidth() / 4;
            int y = img.getHeight();
            for (int i = 0; i < resource.getRate(); i++) {
                int x = (i % 4) * baseX;
                y = (i % 4 == 0) ? y - 8 : y;
                g.drawImage(ball, x, y);
            }
            g.flush();


        } catch (SlickException ex) {
            Logger.getLogger(FactorySpecification.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return img;
    }
}
