/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import simplex.entity.Connection;
import simplex.entity.Consumer;
import simplex.entity.Factory;
import simplex.entity.Node;

/**
 *
 * @author Emil
 */
public class GameState extends BasicGameState{
    
    private final int stateId;
    
    private List<Node> nodes = new LinkedList<>();

    public GameState(int stateId) {
        this.stateId = stateId;
    }
 
    
    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    
        Node n1 = new Node(gc);
        Node n2 = new Node(gc);
        Node n3 = new Node(gc);
        Node n4 = new Node(gc);
        
        new Connection(gc, n1, n2);
        new Connection(gc, n2, n3);
        new Connection(gc, n2, n4);
        new Connection(gc, n3, n4);
        
        n1.setLocation(100, 100);
        n2.setLocation(200, 100);
        n3.setLocation(100, 200);
        n4.setLocation(200, 200);
        
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
            throws SlickException {
        for(Node node : nodes){
            node.render(gc, g);
        }
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) 
            throws SlickException {
        
    }
}
