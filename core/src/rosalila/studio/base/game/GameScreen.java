package rosalila.studio.base.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by turupawn on 4/21/17.
 */
public class GameScreen implements Screen {
    int stage_number;
    Player player;
    Texture player_win;
    Texture player_lost;
    Texture tutorial_swipe;
    Texture tutorial_swipe_left;
    Texture tutorial_swipe_right;
    SpriteBatch sprite_batch;

    private OrthographicCamera hud_camera;

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
        player_win = new Texture("player_win.png");
        player_lost = new Texture("player_lost.png");
        tutorial_swipe = new Texture("tutorial_swipe.png");
        tutorial_swipe_left = new Texture("tutorial_swipe_left.png");
        tutorial_swipe_right = new Texture("tutorial_swipe_right.png");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT);
        camera.position.x = 48*7+48/2;

        setupCamera(w, h);

        Gdx.input.setInputProcessor(null);

        player = new Player();
        Globals.game_state = "running";
        sprite_batch = new SpriteBatch();

        hud_camera = new OrthographicCamera(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT);
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
            sprite_batch.draw(player_win,-720/2,-1280/2);
            sprite_batch.end();
        }
        if(Globals.game_state.equals("lost"))
        {
            sprite_batch.begin();
            sprite_batch.draw(player_lost,-720/2,-1280/2);
            sprite_batch.end();
        }
        if(Globals.game_state.equals("tutorial"))
        {
            if(player.position.x < 48*3)
            {
                sprite_batch.begin();
                sprite_batch.draw(tutorial_swipe_right,-720/2,-1280/2);
                sprite_batch.end();
            }
            else if(player.position.x > 48*12)
            {
                sprite_batch.begin();
                sprite_batch.draw(tutorial_swipe_left,-720/2,-1280/2);
                sprite_batch.end();
            }
            else
            {
                sprite_batch.begin();
                sprite_batch.draw(tutorial_swipe,-720/2,-1280/2);
                sprite_batch.end();
            }
        }
    }

    private void update (float deltaTime)
    {
        if (deltaTime == 0) return;

        if (deltaTime > 0.1f)
            deltaTime = 0.1f;

        player.update(deltaTime);

        float velocity = 15;

        if(Globals.game_state.equals("running"))
        {
            tileCollisionValidation();
            camera.position.y += velocity;
            player.position.y = camera.position.y - 12*48;//- 13*48;
        }

        if(Globals.game_state.equals("won"))
        {
            player.position.y += velocity;
        }

        if((Globals.game_state.equals("won") || Globals.game_state.equals("lost")) && Gdx.input.isTouched())
        {
            Globals.game.setScreen(Globals.stage_selector_screen);
        }
    }

    void tileCollisionValidation()
    {
        Rectangle player_rect = rectPool.obtain();
        player_rect.set(player.position.x / 48, player.position.y / 48, player.WIDTH, player.HEIGHT);

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
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
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
                Globals.game_state = "won";
                Globals.playServices.unlockAchievement(Globals.achievement_ids.get(stage_number));
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
