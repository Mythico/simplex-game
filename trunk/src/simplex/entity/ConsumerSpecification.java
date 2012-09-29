package simplex.entity;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import simplex.util.ImageManager;

/**
 * The Factory Specification describes the inner workings of a factory.
 *
 * @author Emil
 * @author Samuel
 */
public class ConsumerSpecification implements NodeSpecification {

    private HashMap<Connection, Resource> resourceMap = new HashMap<>();
    private Resource expectedResource = new Resource();
    private boolean happy = false;
    private Image img = ImageManager.consumer_node;

    @Override
    public Image getImage() {
        return img;
    }

    public void setExpectedResource(Resource resource) {
        
        if(resource.getType() == this.expectedResource.getType() &&
                resource.getRate() == this.expectedResource.getRate()){
            return;
        }
        this.expectedResource = resource;

        try {
            img = new Image(32, 32);
            Graphics g = img.getGraphics();
            Image consumer = happy
                    ? ImageManager.happy_consumer_node
                    : ImageManager.consumer_node;
            g.drawImage(consumer, 0, 0, Color.white);

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
            Logger.getLogger(FactorySpecification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setResource(Resource resource, Connection conn) {
        resourceMap.put(conn, resource);
        Resource re = new Resource();
        for (Resource r : resourceMap.values()) {
            re.add(r);
        }
        
        if(!happy && expectedResource.getType() == re.getType()
                && expectedResource.getRate() <= re.getRate()){
            happy = true;
        }
    }

    @Override
    public Resource getResource() {
        return null;
    }

    public boolean isHappy() {
        return happy;
    }
}
