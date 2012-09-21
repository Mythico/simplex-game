
/*
 * LoadingTest.java
 *
 * Created on March 30, 2008, 3:45 PM
 */

package mdes.oxy.test;

import java.io.IOException;
import mdes.oxy.*;
import org.newdawn.slick.*;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 *
 * @author davedes
 */
public class LoadingTest extends StateBasedGame {
    
    public static void main(String[] args) throws Exception {
        AppGameContainer app = new AppGameContainer(new LoadingTest());
        app.setDisplayMode(800,600,false);
        app.start();
    }
    
    /**
     * Creates a new instance of LoadingTest
     */
    public LoadingTest() {
        super("LoadingScreen");
    }
    
    Image background;
    Sound sound;
    Sound sound2;
    Image image;
    
    public void initStatesList(GameContainer container) throws SlickException {
        try {
            Desktop.setSkin(new SkinDoc("res/skin/aqua.xml"));
        } catch (OxyException e) {
            e.printStackTrace();
            throw new SlickException("cannot load Oxy");
        }
        
        background = new Image("testdata/sky.jpg");
               
        this.addState(new LoadingState());
        this.addState(new StartState());
        this.enterState(LoadingState.ID);
    }
    
    public void keyPressed(int key, char c) {
        super.keyPressed(key, c);
        if (key == Input.KEY_ESCAPE)
            this.getContainer().exit();
    }
    
    public class LoadingState extends BasicGameState {
        public static final int ID = 1;
        
        private Desktop desktop;
        private Button okButton;
        private Window window;
        private Slider progressBar;
        
	/** The next resource to load */
	private DeferredResource nextResource;
        private boolean started = false; 
        private Timer beginTimer = new Timer(350);
        private boolean loading = false;
        
        public int getID() {
            return ID;
        }

        public void init(GameContainer container, StateBasedGame game) throws SlickException {
            try {
                desktop = Desktop.parse(LoadingState.this, container, "testdata/LoadingScreen.xml");
            } catch (OxyException e) {
                e.printStackTrace();
                System.exit(1);
            }
            OxyDoc doc = desktop.getDoc();
            window = (Window)doc.getElement("loadBox");
            okButton = (Button)doc.getElement("okButton");
            progressBar = (Slider)doc.getElement("pbar");     
            beginTimer.setRepeats(false);
            
            //load elements
            LoadingList.setDeferredLoading(true);
            sound = new Sound("testdata/cbrown01.wav");
            sound2 = new Sound("testdata/engine.wav");
            new Sound("testdata/restart.ogg");

            new Image("testdata/cursor.png");
            new Image("testdata/cursor.tga");
            image = new Image("testdata/dungeontiles.gif");
            new Image("testdata/logo.gif");
        }
        
        public void enter(GameContainer container, StateBasedGame game) throws SlickException {
            super.enter(container, game);
            window.setVisible(true);
            beginTimer.start();
        }
        
        ////// OXY EVENT: okButton action ///////
        public void doOK() {
            window.setVisible(false);
            beginTimer.restart();
        }
        
        public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
            background.draw(0, 0, container.getWidth(), container.getHeight());
            desktop.render(g);
        }
        
        public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
            beginTimer.update(container, delta);
            //a delay is used to keep things running smoothly
            if (beginTimer.isAction()) {
                if (started) {
                    game.enterState(StartState.ID);
                } else
                    loading = true;
            }
            
            if (loading) {
                if (nextResource != null) {
                    try {
                        nextResource.load();
                        // slow down loading for example purposes
                        try { Thread.sleep(50); } catch (Exception e) {}
                    } catch (IOException e) {
                        throw new SlickException("Failed to load: "+nextResource.getDescription(), e);
                    }
                    nextResource = null;
                }

                LoadingList list = LoadingList.get();
                if (list.getRemainingResources() > 0) {
                    nextResource = LoadingList.get().getNext();
                } else {
                    if (!started) {
                        started = true;
                        loading = false;
                        okButton.setEnabled(true);
                    }
                }
                
                //UPDATE PROGRESS BAR
                int total = list.getTotalResources();
                int loaded = total - list.getRemainingResources();
                float val = loaded / (float)total;
                progressBar.setValue(val);
            }
            
            //UPDATE DESKTOP
            desktop.update(delta);
        }
    }
    
    public class StartState extends BasicGameState {
        public static final int ID = 2;
        
        private Desktop desktop;
        private LightBox lightBox;
        
        public int getID() {
            return ID;
        }

        public void init(GameContainer container, StateBasedGame game) throws SlickException {
            try {
                desktop = Desktop.parse(StartState.this, container, "testdata/StartScreen.xml");
            } catch (OxyException e) {
                e.printStackTrace();
                System.exit(1);
            }
            
            lightBox = (LightBox)desktop.getDoc().getElement("lightBox");
        }
        
        
        
        public void handleAction(int type) {
            if (type==3) {
                lightBox.showImage(image);
            } else {
                Sound s = (type==1) ? sound : sound2;
                s.stop();
                s.play();
            }
        }

        public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
            background.draw(0, 0, container.getWidth(), container.getHeight());
            desktop.render(g);
        }

        public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
            desktop.update(delta);
        }
    }
}
