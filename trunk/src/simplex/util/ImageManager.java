package simplex.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import simplex.entity.Resource;
import simplex.entity.specification.CombinerSpecification;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.EaterSpecification;
import simplex.entity.specification.FactorySpecification;
import simplex.entity.specification.NodeSpecification;
import simplex.entity.specification.SplitterSpecification;

/**
 * An Image manager for loading images.
 *
 * @author Emil
 * @author Samuel
 */
public class ImageManager {

    private static Image dummy_node;
    private static Image factory_node;
    private static Image consumer_node;
    private static Image happy_consumer_node;
    private static Image eater_node;
    private static Image splitter_node;
    private static Image combiner_node;
    public static Image connection_swap_left_icon;
    public static Image connection_swap_right_icon;
    private static Image white_resource;
    private static Image red_resource;
    private static Image green_resource;
    private static Image blue_resource;
    private static Map<NodeSpecification, Map<Resource, Image>> nodeResourceMap;
    private static Map<Integer, Image> redResourceMap;
    private static Map<Integer, Image> blueResourceMap;
    private static Map<Integer, Image> greenResourceMap;

    /**
     * Initiates all the statically created images.
     * This method must be called in the main initialization method.
     * @throws SlickException 
     */
    public static void init() throws SlickException {
        dummy_node = new Image("img/dummy_node.png", Color.white);
        factory_node = new Image("img/factory_node.png", Color.white);
        consumer_node = new Image("img/consumer_node.png", Color.white);
        happy_consumer_node = new Image("img/happy_consumer_node.png", Color.white);
        eater_node = new Image("img/eater_node.png", Color.white);
        splitter_node = new Image("img/splitter_node.png", Color.white);
        combiner_node = new Image("img/combiner_node.png", Color.white);

        connection_swap_left_icon = new Image("img/connection_swap_left.png", Color.white);
        connection_swap_right_icon = new Image("img/connection_swap_right.png", Color.white);

        white_resource = new Image("img/white_resource.png");
        red_resource = new Image("img/red_resource.png");
        green_resource = new Image("img/green_resource.png");
        blue_resource = new Image("img/blue_resource.png");

        nodeResourceMap = new HashMap<>();
        redResourceMap = new HashMap<>();
        blueResourceMap = new HashMap<>();
        greenResourceMap = new HashMap<>();
    }

    /**
     * Check if there exist a cashed copy of an image representing the resource,
     * otherwise creates a new image and save it in the cash.
     * @param resource The resource the image will be created from.
     * @return Image representing a resource.
     */
    public static Image get(Resource resource) {
        switch (resource.getType()) {
            case Resource.RED:
                return getScaled(red_resource, resource.getRate(), redResourceMap);
            case Resource.BLUE:
                return getScaled(blue_resource, resource.getRate(), blueResourceMap);
            case Resource.GREEN:
                return getScaled(green_resource, resource.getRate(), greenResourceMap);
            default:
                return white_resource;
        }
    }

    /**
     * Get a scaled Image with a from a specified resourceMap.
     * @param source The source image to fetch the scaled image.
     * @param size The scale of the image.
     * @param map The map where scaled images resides in.
     * @return A scaled Image created from the source image.
     */
    private static Image getScaled(Image source, int size, Map<Integer, Image> map) {
        Image img = map.get(size);
        if (img == null) {
            img = source.getScaledCopy(0.5f * size);
            map.put(size, img);
        }
        return img;
    }

    /**
     * Get the Image of a node specification.
     * @param spec The node specification.
     * @return An Image representing the specified NodeSpecification.
     */
    public static Image get(NodeSpecification spec) {
        final Resource r;
        if (spec instanceof ConsumerSpecification) {
            r = ((ConsumerSpecification) spec).getExpectedResource();
        } else if (spec instanceof FactorySpecification) {
            r = ((FactorySpecification) spec).getResource();
        } else if (spec instanceof EaterSpecification) {
            return eater_node;
        } else if (spec instanceof SplitterSpecification) {
            return splitter_node;
        } else if (spec instanceof CombinerSpecification) {
            return combiner_node;
        } else {
            return dummy_node;
        }


        Map<Resource, Image> resourceMap = nodeResourceMap.get(spec);

        if (resourceMap == null) {
            resourceMap = new HashMap<>();
            nodeResourceMap.put(spec, resourceMap);
        }

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
            } else {
                img = createResourceImage(dummy_node, r);
            }
            resourceMap.put(r, img);
        }
        return img;
    }

    /**
     * Create an image of a NodeSpecification with a specified resource on it.
     * @param background The image that will be the background of the new Image.
     * @param resource The resouce that will be added to the Image.
     * @return A new Image with a resource painted on it.
     */
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
