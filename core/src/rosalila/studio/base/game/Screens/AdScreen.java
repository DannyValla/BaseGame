package rosalila.studio.base.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import rosalila.studio.base.game.Buttons.AdBackButton;
import rosalila.studio.base.game.Buttons.AdPlayButton;
import rosalila.studio.base.game.Buttons.BackButton;
import rosalila.studio.base.game.Buttons.AdButton;
import rosalila.studio.base.game.Buttons.LeftButton;
import rosalila.studio.base.game.Buttons.PlayServicesButton;
import rosalila.studio.base.game.Buttons.RightButton;
import rosalila.studio.base.game.Buttons.StageButtonActor;
import rosalila.studio.base.game.Globals;

/**
 * Created by turupawn on 7/11/17.
 */
public class AdScreen implements Screen {

    Stage stage;
    SpriteBatch sprite_batch;
    OrthographicCamera camera;
    Texture background;
    Texture thanks;

    @Override
    public void show() {
        this.sprite_batch = new SpriteBatch();

        Globals.support_button_pressed = false;

        camera = new OrthographicCamera(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT);
        ExtendViewport viewp = new ExtendViewport(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT, camera); // change this to your needed viewport
        stage = new Stage(viewp,sprite_batch);

        background = new Texture("ad_screen_background.png");
        thanks = new Texture("thanks.png");

        Gdx.input.setInputProcessor(stage);

        //stage.addActor(new AdButton(sprite_batch));
        stage.addActor(new AdBackButton(sprite_batch));
        stage.addActor(new AdPlayButton(sprite_batch));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 1, 1, 1, 1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);

        sprite_batch.begin();
        sprite_batch.draw(background, 0, 0);
        if(Globals.support_button_pressed)
            sprite_batch.draw(thanks, 29, 199);
        sprite_batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}