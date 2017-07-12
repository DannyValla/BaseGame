package rosalila.studio.base.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import rosalila.studio.base.game.Buttons.AdButton;
import rosalila.studio.base.game.Globals;
import rosalila.studio.base.game.Buttons.LeftButton;
import rosalila.studio.base.game.Buttons.PlayServicesButton;
import rosalila.studio.base.game.Buttons.RightButton;
import rosalila.studio.base.game.Buttons.StageButtonActor;

/**
 * Created by turupawn on 4/21/17.
 */
public class StageSelectorScreen implements Screen {

    Stage stage;
    SpriteBatch sprite_batch;
    OrthographicCamera camera;
    Texture background;

    @Override
    public void show() {
        this.sprite_batch = new SpriteBatch();

        camera = new OrthographicCamera(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT);
        ExtendViewport viewp = new ExtendViewport(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT, camera); // change this to your needed viewport
        stage = new Stage(viewp,sprite_batch);

        background = new Texture("stage_select_background.png");

        Gdx.input.setInputProcessor(stage);
        for(int i=0;i<Globals.total_stages;i++)
        {
            StageButtonActor stage_button_actor = new StageButtonActor(i + 1,sprite_batch);
            stage.addActor(stage_button_actor);
        }
        stage.addActor(new LeftButton(sprite_batch));
        stage.addActor(new RightButton(sprite_batch));

        stage.addActor(new PlayServicesButton(sprite_batch));
        stage.addActor(new AdButton(sprite_batch));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 1, 1, 1, 1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);

        

        sprite_batch.begin();
        sprite_batch.draw(background,0,0);
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
