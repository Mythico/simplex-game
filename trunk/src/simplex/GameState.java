/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.util.LinkedList;
import java.util.List;
import javax.swing.text.Position;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import simplex.entity.Connection;
import simplex.entity.Node;

/**
 *
 * @author Emil
 */
public class GameState extends BasicGameState{
    
    private final int stateId;
    
    private List<Node> factories = new LinkedList<>();

    public GameState(int stateId) {
        this.stateId = stateId;        
    }
 
    
    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    
	
        Node n1 = new Node(100,100);
        Node n2 = new Node(200, 100);
        Node n3 = new Node(100, 200);
        Node n4 = new Node(200, 200);
        
	
	
        Connection c1 = new Connection(n2);
        Connection c2 = new Connection(n3);
        Connection c3 = new Connection(n4);
        Connection c4 = new Connection(n4);
        
	n1.addConnection(c1);
	n2.addConnection(c2);
	n3.addConnection(c3);
	n2.addConnection(c4);
        
	//TODO: fulhack bort!!
	n2.rate = 2;
	
        factories.add(n1);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
            throws SlickException {
        for(Node node : factories){
            node.render(g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) 
            throws SlickException {
	
        for(Node node : factories){
            node.update(delta);
        }
    }
}
