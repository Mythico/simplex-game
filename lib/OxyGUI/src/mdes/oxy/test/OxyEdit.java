/*
 * OxyEdit.java
 *
 * Created on May 14, 2008, 6:04 PM
 */

package mdes.oxy.test;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import mdes.oxy.*;
import org.newdawn.slick.*;
import org.xml.sax.InputSource;

/**
 *
 * @author davedes
 */
public class OxyEdit extends JFrame {
    
    public static void main(String[] args) throws Exception {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception e) {}
        new OxyEdit();
    }
    
    private EditInput input;
    private CanvasGameContainer container;
    private EditGame game;
    
    public OxyEdit() throws SlickException {
        super("Oxy Editor");
        game = new EditGame();
        container = new CanvasGameContainer(game);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setSize(640, 480);
        setLocation(0, 0);
        getContentPane().add(container);
        
        input = new EditInput(this);
        input.setLocation(getWidth(), 0);
        input.setSize(Toolkit.getDefaultToolkit().getScreenSize().width - getWidth(), input.getHeight());
                
        createMenus();
        
        setVisible(true);
        container.start();
    }
    
    public void setApply() {
        game.reload = true;
    }
    
    private void createMenus() {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        JMenuBar bar = new JMenuBar();
        
        final Object[] options = new Object[] {
            "Apply Changes", "Discard Changes"
        };
        
        JMenu file = new JMenu("File");
        JMenuItem create = new JMenuItem("Edit Desktop");
        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                input.setVisible(true);
            }
        });
        file.add(create);
                
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(quit);
        
        bar.add(file);
        
        setJMenuBar(bar);
    }
    
    public class EditGame extends BasicGame implements Desktop.WarningHandler {
        
        /** Creates a new instance of OxyEdit */
        public EditGame() {
            super("OxyEdit");
        }
        
        Desktop desktop;
        boolean reload = false;
        
        public void handleWarning(String msg, Throwable t) {
            input.error(msg);
        }
        
        public void init(GameContainer container) throws SlickException {
            Desktop.setWarningHandler(this);
        }
        
        public void showMessage(String text) {
            input.message(text);
        }
        
        public void showDialog() {
            if (desktop!=null && desktop.getModalWindow()!=null) {
                desktop.getModalWindow().setVisible(true);
            }
        }
        
        public void update(GameContainer container, int delta) throws SlickException {
            if (reload) {
                Desktop old = this.desktop;
                try {
                    String docStr = input.getText();
                    InputSource in = new InputSource(new StringReader(docStr));
                    OxyDoc doc = new OxyDoc(this, OxyEdit.this.container.getContainer(), in);
                    desktop = doc.getDesktop();
                } catch (Exception e) {
                    desktop = old;
                    input.error(e.getMessage());
                }
                reload = false;
            }
            
            if (desktop != null)
                desktop.update(delta);
        }

        public void render(GameContainer container, Graphics g) throws SlickException {
            if (desktop != null)
                desktop.render(g);
        }
        
    }
}

/*<desktop opaque="true" background="#ababab">
    <panel bounds="50px, 15%, 50%, 50%" background="#f0f0f0" opaque="true">
	<button action="showMessage('Hello, World!')" text="Click Me!" 
                centeringEnabled="true" location="50%, 50%" />
    </panel>
</desktop>

<desktop modalWindow="messageBox" opaque="true" background="#ababab">
    <panel bounds="50px, 15%, 50%, 25%" background="#f0f0f0" opaque="true">
	<button action="showDialog()" text="Show Dialog" 
                centeringEnabled="true" location="50%, 50%" />
    </panel>
    
    <dialog name="messageBox" size="250px, 100px" title="Dialog" 
            location="50%, 50%" centeringEnabled="true" />
</desktop>*/