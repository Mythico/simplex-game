package simplex;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class Main extends BasicGame{

    private World world;
    private Display display;
    private Input input;
 
    public Main()
    {
        super("Slick2DPath2Glory - SimpleGame");
        
        world = new World();
        display = new Display(world);
        input = new Input(world);
        
    }
 
    @Override
    public void init(GameContainer gc) 
			throws SlickException {
 
    }
 
    @Override
    public void update(GameContainer gc, int delta) 
			throws SlickException     
    {
        world.update(gc, delta);
    }
 
    public void render(GameContainer gc, Graphics g) 
			throws SlickException 
    {
        display.render(gc, g);
    }
 
    public static void main(String[] args) 
			throws SlickException
    {
         AppGameContainer app = 
			new AppGameContainer(new Main());
 
         app.setDisplayMode(800, 600, false);
         app.start();
    }
}