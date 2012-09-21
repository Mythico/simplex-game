/*
 * OxyException.java
 *
 * Created on January 24, 2008, 11:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mdes.oxy;

/**
 *
 * @author davedes
 */
public class OxyException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>OxyException</code> without detail message.
     */
    public OxyException() {
    }
    
    
    /**
     * Constructs an instance of <code>OxyException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public OxyException(String msg) {
        super(msg);
    }
}
