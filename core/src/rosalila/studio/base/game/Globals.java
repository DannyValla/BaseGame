package rosalila.studio.base.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by turupawn on 4/22/17.
 */
public class Globals {
    public static final int VIRTUAL_WIDTH = 720;
    public static final int VIRTUAL_HEIGHT = 1280;
    public static final float ASPECT_RATIO =
            (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
    public static Rectangle viewport;

    public static PlayServices playServices;
    public static MyGdxGame game;
    public static StageSelectorScreen stage_selector_screen;
    public static String game_state;

    public static Map<Integer,String> achievement_ids;

    public static Preferences preferences;

    public static void init()
    {
        Globals.stage_selector_screen = new StageSelectorScreen();
        achievement_ids = new HashMap<Integer, String>();
        achievement_ids.put(1,"CgkI9oynn-wYEAIQAg");
        achievement_ids.put(2,"CgkI9oynn-wYEAIQAw");
        achievement_ids.put(3,"CgkI9oynn-wYEAIQBA");
        achievement_ids.put(4,"CgkI9oynn-wYEAIQBQ");
        achievement_ids.put(5,"CgkI9oynn-wYEAIQBg");
        achievement_ids.put(6,"CgkI9oynn-wYEAIQBw");
        achievement_ids.put(7,"CgkI9oynn-wYEAIQCA");
        achievement_ids.put(8,"CgkI9oynn-wYEAIQCQ");
        achievement_ids.put(9,"CgkI9oynn-wYEAIQCg");
        achievement_ids.put(10,"CgkI9oynn-wYEAIQCw");
        achievement_ids.put(11,"CgkI9oynn-wYEAIQDA");
        achievement_ids.put(12,"CgkI9oynn-wYEAIQDQ");

        preferences = Gdx.app.getPreferences("GamePreferences");
    }
}
