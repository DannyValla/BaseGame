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
 * Created by turupawn on 4/30/17.
 */
public class PlayServicesButton extends Actor {

    Texture icon;
    SpriteBatch sprite_batch;
    int x;
    int y;
    int width;
    int height;
    boolean is_down;

    public PlayServicesButton(SpriteBatch sprite_batch)
    {
        this.sprite_batch = sprite_batch;
        this.icon = new Texture(Gdx.files.internal("play_services.png"));
        this.is_down = false;

        width = this.icon.getWidth();
        height = this.icon.getHeight();

        x = 0;
        y = 800;

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
                Globals.playServices.showAchievementsIntent();
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
        sprite_batch.begin();
        if(is_down)
        {
            sprite_batch.setColor(1,1,1,0.5f);
            sprite_batch.draw(icon, x, y);
            sprite_batch.setColor(1,1,1,1);
        }else
        {
            sprite_batch.draw(icon, x, y);
        }
        sprite_batch.end();
    }
}
