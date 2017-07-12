package rosalila.studio.base.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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
    public static rosalila.studio.base.game.Screens.StageSelectorScreen stage_selector_screen;
    public static rosalila.studio.base.game.Screens.AdScreen ad_screen;
    public static String game_state;

    public static Map<Integer,String> achievement_ids;

    public static Preferences preferences;

    public static Map<String,Sound> sounds;

    public static int current_stage_page = 0;
    public static int stages_per_page = 9;
    public static int total_stages = 27;

    public static Texture player_win;
    public static Texture player_lost;
    public static Texture tutorial_swipe;
    public static Texture tutorial_swipe_left;
    public static Texture tutorial_swipe_right;

    public static boolean support_button_pressed;

    public static void init()
    {
        stage_selector_screen = new rosalila.studio.base.game.Screens.StageSelectorScreen();
        ad_screen = new rosalila.studio.base.game.Screens.AdScreen();

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
        achievement_ids.put(13,"CgkI9oynn-wYEAIQDw");
        achievement_ids.put(14,"CgkI9oynn-wYEAIQEA");
        achievement_ids.put(15,"CgkI9oynn-wYEAIQEQ");
        achievement_ids.put(16,"CgkI9oynn-wYEAIQEg");
        achievement_ids.put(17,"CgkI9oynn-wYEAIQEw");
        achievement_ids.put(18,"CgkI9oynn-wYEAIQFA");
        achievement_ids.put(19,"CgkI9oynn-wYEAIQFQ");
        achievement_ids.put(20,"CgkI9oynn-wYEAIQFg");
        achievement_ids.put(21,"CgkI9oynn-wYEAIQFw");
        achievement_ids.put(22,"CgkI9oynn-wYEAIQGA");
        achievement_ids.put(23,"CgkI9oynn-wYEAIQGQ");
        achievement_ids.put(24,"CgkI9oynn-wYEAIQGg");
        achievement_ids.put(25,"CgkI9oynn-wYEAIQGw");
        achievement_ids.put(26,"CgkI9oynn-wYEAIQHA");
        achievement_ids.put(27,"CgkI9oynn-wYEAIQHQ");

        preferences = Gdx.app.getPreferences("GamePreferences");

        sounds = new HashMap<String, Sound>();
        sounds.put("select", Gdx.audio.newSound(Gdx.files.internal("sfx/select.ogg")));
        sounds.put("back", Gdx.audio.newSound(Gdx.files.internal("sfx/back.ogg")));
        sounds.put("win", Gdx.audio.newSound(Gdx.files.internal("sfx/win.ogg")));
        sounds.put("lose", Gdx.audio.newSound(Gdx.files.internal("sfx/lose.ogg")));
        sounds.put("left", Gdx.audio.newSound(Gdx.files.internal("sfx/left.ogg")));
        sounds.put("right", Gdx.audio.newSound(Gdx.files.internal("sfx/right.ogg")));

        player_win = new Texture("player_win.png");
        player_lost = new Texture("player_lost.png");
        tutorial_swipe = new Texture("tutorial_swipe.png");
        tutorial_swipe_left = new Texture("tutorial_swipe_left.png");
        tutorial_swipe_right = new Texture("tutorial_swipe_right.png");

        support_button_pressed = false;
    }
}
