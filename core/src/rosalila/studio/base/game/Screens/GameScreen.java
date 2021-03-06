package rosalila.studio.base.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import rosalila.studio.base.game.Buttons.BackButton;
import rosalila.studio.base.game.Globals;
import rosalila.studio.base.game.Buttons.RestartButton;

/**
 * Created by turupawn on 4/21/17.
 */
public class GameScreen implements Screen {
    int stage_number;
    Player player;
    SpriteBatch sprite_batch;

    final Vector2 current_shake_position = new Vector2(0,0);
    int shake_magnitude = 50;
    int shake_duration_left = 0;

    OrthographicCamera hud_camera;
    Stage hud_stage;
    SpriteBatch hud_batch;

    boolean exit_touch_down;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject () {
            return new Rectangle();
        }
    };
    private Array<Rectangle> tiles = new Array<Rectangle>();

    public GameScreen(int stage_number)
    {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        this.stage_number = stage_number;
        map = new TmxMapLoader().load(stage_number+".tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT);
        camera.position.x = 48*7+48/2;

        setupCamera(w, h);

        Gdx.input.setInputProcessor(null);

        player = new Player();
        Globals.game_state = "running";
        sprite_batch = new SpriteBatch();

        this.hud_batch = new SpriteBatch();
        hud_camera = new OrthographicCamera(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT);
        ExtendViewport hud_viewport = new ExtendViewport(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT, hud_camera);
        hud_stage = new Stage(hud_viewport,hud_batch);

        exit_touch_down = false;
    }

    @Override
    public void render (float delta)
    {
        // update camera
        camera.update();
        //camera.apply(Gdx.gl20);

        // set viewport
        Gdx.gl.glViewport((int) Globals.viewport.x, (int) Globals.viewport.y,
                (int) Globals.viewport.width, (int) Globals.viewport.height);

        // clear previous frame
        Gdx.gl.glClearColor(52f / 255f, 73f / 255f, 94f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();
        update(deltaTime);

        camera.update();
        renderer.setView(camera);
        renderer.render();
        player.render(deltaTime, renderer);

        sprite_batch.setProjectionMatrix(hud_camera.combined);
        if(Globals.game_state.equals("won"))
        {
            sprite_batch.begin();
            sprite_batch.draw(Globals.player_win,0,0);
            sprite_batch.end();
        }
        if(Globals.game_state.equals("lost"))
        {
            sprite_batch.begin();
            sprite_batch.draw(Globals.player_lost,0,0);
            sprite_batch.end();
        }
        if(Globals.game_state.equals("tutorial"))
        {
            if(player.position.x < 48*3)
            {
                sprite_batch.begin();
                sprite_batch.draw(Globals.tutorial_swipe_right,0,0);
                sprite_batch.end();
            }
            else if(player.position.x > 48*12)
            {
                sprite_batch.begin();
                sprite_batch.draw(Globals.tutorial_swipe_left,0,0);
                sprite_batch.end();
            }
            else
            {
                sprite_batch.begin();
                sprite_batch.draw(Globals.tutorial_swipe,0,0);
                sprite_batch.end();
            }
        }

        hud_stage.draw();
        hud_stage.act();
    }

    private void update (float deltaTime)
    {
        if (deltaTime == 0) return;

        if (deltaTime > 0.1f)
            deltaTime = 0.1f;

        player.update(deltaTime);

        float velocity = 15;

        if(shake_duration_left>0)
        {
            camera.position.x-=current_shake_position.x;
            camera.position.y-=current_shake_position.y;
            current_shake_position.x = ((float)Math.random()*shake_magnitude)%shake_magnitude;
            current_shake_position.y = ((float)Math.random()*shake_magnitude)%shake_magnitude;
            camera.position.x+=current_shake_position.x;
            camera.position.y+=current_shake_position.y;
            shake_duration_left--;
        }

        if(Globals.game_state.equals("running"))
        {
            camera.position.y += velocity;
            player.position.y = camera.position.y - 12*48;//- 13*48;
            tileCollisionValidation();
        }

        if(Globals.game_state.equals("won"))
        {
            player.position.y += velocity;
        }

        if(Globals.game_state.equals("won") && Gdx.input.isTouched())
        {
            exit_touch_down = true;
        }

        if(exit_touch_down && !Gdx.input.isTouched())
        {
            Globals.game.setScreen(Globals.stage_selector_screen);
        }
    }

    void tileCollisionValidation()
    {
        Rectangle player_rect = rectPool.obtain();
        player_rect.set(player.position.x / 48 + 0.125f, player.position.y / 48 + 0.125f, player.WIDTH, player.HEIGHT);

        getWallTiles((int) player_rect.x, (int) player_rect.y,
                (int) player_rect.x + (int) player_rect.width, (int) player_rect.y + (int) player_rect.height,
                tiles, player_rect);


        getFinishTiles((int) player_rect.x, (int) player_rect.y,
                (int) player_rect.x + (int) player_rect.width, (int) player_rect.y + (int) player_rect.height,
                tiles, player_rect);


        getTutorialTiles((int) player_rect.x, (int) player_rect.y,
                (int) player_rect.x + (int) player_rect.width, (int) player_rect.y + (int) player_rect.height,
                tiles, player_rect);

        rectPool.free(player_rect);
    }

    private void getWallTiles (int startX, int startY, int endX, int endY, Array<Rectangle> tiles, Rectangle player_rect)
    {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("walls");
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++)
        {
            for (int x = startX; x <= endX; x++)
            {
                Cell cell = layer.getCell(x, y);
                if (cell != null)
                {
                    Globals.sounds.get("lose").play();
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                    shake_duration_left = 25;
                    Gdx.input.setInputProcessor(hud_stage);
                    hud_stage.addActor(new RestartButton(stage_number));
                    hud_stage.addActor(new BackButton());
                }
            }
        }

        for (Rectangle tile : tiles) {

            if (player_rect.overlaps(tile)) {
                Globals.game_state = "lost";
            }
        }
    }

    private void getFinishTiles (int startX, int startY, int endX, int endY, Array<Rectangle> tiles, Rectangle player_rect)
    {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("finish");
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++)
        {
            for (int x = startX; x <= endX; x++)
            {
                Cell cell = layer.getCell(x, y);
                if (cell != null)
                {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }

        for (Rectangle tile : tiles) {

            if (player_rect.overlaps(tile)) {
                Globals.sounds.get("win").play();
                Globals.game_state = "won";
                if(!Globals.preferences.getBoolean("level" + stage_number + "_complete"))
                {
                    Globals.preferences.putBoolean("level" + stage_number + "_complete", true);
                    Globals.preferences.flush();
                    Globals.playServices.unlockAchievement(Globals.achievement_ids.get(stage_number));
                }
            }
        }
    }

    private void getTutorialTiles (int startX, int startY, int endX, int endY, Array<Rectangle> tiles, Rectangle player_rect)
    {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("tutorial");
        if(layer==null)
        {
            tiles.clear();
            return;
        }
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++)
        {
            for (int x = startX; x <= endX; x++)
            {
                Cell cell = layer.getCell(x, y);
                if (cell != null)
                {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }

        for (Rectangle tile : tiles) {

            if (player_rect.overlaps(tile)) {
                layer.setCell((int)tile.x, (int)tile.y, null);
                Globals.game_state = "tutorial";
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height)
    {
        setupCamera(width, height);
    }

    void setupCamera(float width, float height)
    {
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);

        if(aspectRatio > Globals.ASPECT_RATIO)
        {
            scale = (float)height/(float)Globals.VIRTUAL_HEIGHT;
            crop.x = (width - Globals.VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < Globals.ASPECT_RATIO)
        {
            scale = (float)width/(float)Globals.VIRTUAL_WIDTH;
            crop.y = (height - Globals.VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)Globals.VIRTUAL_WIDTH;
        }

        float w = (float)Globals.VIRTUAL_WIDTH*scale;
        float h = (float)Globals.VIRTUAL_HEIGHT*scale;
        Globals.viewport = new Rectangle(crop.x, crop.y, w, h);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose () {
    }
}
