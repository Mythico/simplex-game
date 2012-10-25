package simplex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * A simple class for saving and loading high scores.
 *
 * @author Emil
 * @author Samuel
 */
final class HighScore {

    private static final Map<String, List<String[]>> scoreMap = new HashMap<>();

    
    private HighScore(){
        
    }
    
    /**
     * Load the high scores from disk.
     */
    static void load() {
        try (BufferedReader in = new BufferedReader(new FileReader("highscore.dat"))) {
            scoreMap.clear();
            String line;
            while ((line = in.readLine()) != null) {
                String[] part = line.split(",");
                if (part.length != 3) {
                    return; //Wrong format
                }
                String level = part[0];
                String time = part[1];
                String clicks = part[2];
                addValue(level, time, clicks);
            }

        } catch (FileNotFoundException ex) {
            //Do nothing
        } catch (IOException ex) {
            Logger.getLogger(HighScore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Adds a value to the scoreMap.
     * @param level The level the values will be added to.
     * @param time The time that will be added.
     * @param clicks Number of clicks that will be added.
     */
    private static void addValue(String level, String time, String clicks) {
        List<String[]> scores = scoreMap.get("level");
        if (scores == null) {
            scores = new LinkedList<>();
            scoreMap.put(level, scores);
        }                
        scores.add(new String[]{time, clicks});
    }

    /**
     * 
     * Saves the time and clicks it took to complete a level.
     * @param level Level the score was gained.
     * @param time The time it took to complete the level.
     * @param clicks The number of clicks it took to complete the level.
     */
    static void save(String level, String time, String clicks) {
        load();
        addValue(level, time, clicks);
        save(scoreMap);
    }
    
    /**
     * Saves the score map to disk.
     * @param scoreMap The score map to be saved.
     */
    private static void save(Map<String, List<String[]>> scoreMap) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter("highscore.dat"))) {
            for(String level : scoreMap.keySet()){
                for(String[] score : scoreMap.get(level)){
                    out.write(level + "," + score[0] + "," + score[1]);
                    out.newLine();
                }
            }
        }catch (IOException ex) {
            Logger.getLogger(HighScore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Render the high scores
     * @param g The graphics to render on.
     */
    static void render(Graphics g) {
        g.setColor(Color.yellow);
        int align_top = 50;
        int align_left_level = 25;
        int align_left_time = 150;
        int align_left_clicks = 250;
        g.drawString("Level", align_left_level, align_top);
        g.drawString("Time", align_left_time, align_top);
        g.drawString("Clicks",align_left_clicks, align_top);

        int i = 1;
        for (String level : scoreMap.keySet()) {
            g.drawString(level, align_left_level, align_top + 50 * i);
            for (String[] score : scoreMap.get(level)) {
                g.drawString(score[0], align_left_time, align_top + 50 * i);
                g.drawString(score[1], align_left_clicks, align_top + 50 * i);
                i++;
            }
        }
    }

    /**
     * Clears the highscore.
     */
    public static void clear() {
        scoreMap.clear();
        save(scoreMap);        
    }

}
