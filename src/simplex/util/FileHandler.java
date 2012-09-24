/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.yaml.snakeyaml.Yaml;

/**
 * 
 * @author Samuel
 */
public class FileHandler {

    public static void saveToFile(Iterator<? extends Object> i, File f) {
        Yaml yaml = new Yaml();
        f.delete();
        f = new File("level.yml");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(f));
            out.print(yaml.dumpAll(i));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Iterable<Object> loadFromFile(File f) {
        Yaml yaml = new Yaml();
        try {
            return yaml.loadAll(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}