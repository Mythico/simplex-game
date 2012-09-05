package simplex;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


 
public class Main extends StateBasedGame{

    public static final int MAINMENUSTATE = 0;
    public static final int GAMESTATE = 1;
    public static final int HELPSTATE = 2;
    public static final int LOADINGSTATE = 3;
    public static final int HIGHSCORESTATE = 4;
 
    public Main()
    {
        super("Simplex");
        
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        addState(new MainMenuState(MAINMENUSTATE));
        addState(new GameState(GAMESTATE));
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