package rosalila.studio.base.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by turupawn on 4/21/17.
 */
public class SplashScreen implements Screen {

    SpriteBatch sprite_batch;
    Texture splash_texture;
    private OrthographicCamera camera;

    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        setupCamera(w, h);
        sprite_batch = new SpriteBatch();
        camera = new OrthographicCamera(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT);
        splash_texture = new Texture(Gdx.files.internal("splash.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glViewport((int) Globals.viewport.x, (int) Globals.viewport.y,
                (int) Globals.viewport.width, (int) Globals.viewport.height);

        sprite_batch.setProjectionMatrix(camera.combined);

        sprite_batch.begin();
        sprite_batch.draw(splash_texture, -720/2, -1280/2);
        sprite_batch.end();

        if(Gdx.input.justTouched()) {
            Globals.game.setScreen(Globals.stage_selector_screen);
        }
    }

    @Override
    public void resize(int width, int height) {
        setupCamera(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

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
}
