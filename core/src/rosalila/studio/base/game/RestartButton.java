package rosalila.studio.base.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Created by turupawn on 5/2/17.
 */
public class RestartButton  extends Actor {

    Texture icon;
    int x;
    int y;
    int width;
    int height;
    boolean is_down;
    int stage_number;

    public RestartButton(final int stage_number)
    {
        this.stage_number = stage_number;

        this.icon = new Texture(Gdx.files.internal("button_retry.png"));
        this.is_down = false;

        width = this.icon.getWidth();
        height = this.icon.getHeight();

        x = 28;
        y = 660;

        super.setBounds(x, y, width,height);
        setTouchable(Touchable.enabled);

        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                is_down = true;
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                is_down = false;
                Globals.sounds.get("select").play();
                Globals.game.setScreen(new GameScreen(stage_number));
            }
        });
    }

    @Override
    public void act(float delta)
    {

    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(is_down) {
            batch.setColor(1, 1, 1, 0.5f);
        }
        batch.draw(icon, x, y);
        batch.setColor(1,1,1,1);
    }
}