package simplex.entity;

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
public class FactorySpecification implements NodeSpecification {

    private Resource resource = new Resource();
    private Image img = ImageManager.factory_node.copy();

    @Override
    public Image getImage() {
        return img;
    }

    @Override
    public void setResource(Resource resource, Connection conn) {
        if(resource.getType() == this.resource.getType() &&
                resource.getRate() == this.resource.getRate()){
            return;
        }
        
        this.resource = resource;                

        //TODO: Remove this resource hog.
        try {
            img = new Image(32, 32);
            Graphics g = img.getGraphics();
            g.drawImage(ImageManager.factory_node, 0, 0, Color.white);
            Image ball = ImageManager.get(resource).getScaledCopy(8, 8);
            int baseX = img.getWidth() / 4;
            int y = img.getHeight();
            for (int i = 0; i < resource.getRate(); i++) {
                int x = (i % 4) * baseX;
                y = (i%4==0) ? y-8 : y;
                g.drawImage(ball, x, y);
            }
            g.flush();
        } catch (SlickException ex) {
            Logger.getLogger(FactorySpecification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Resource getResource() {
        return resource;
    }
}
