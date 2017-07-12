package rosalila.studio.base.game.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import rosalila.studio.base.game.Globals;

/**
 * Created by turupawn on 7/11/17.
 */
public class AdPlayButton extends Actor {

    Texture icon;
    SpriteBatch sprite_batch;
    int x;
    int y;
    int width;
    int height;
    boolean is_down;

    public AdPlayButton(SpriteBatch sprite_batch)
    {
        this.sprite_batch = sprite_batch;
        this.icon = new Texture(Gdx.files.internal("play_black_button.png"));
        this.is_down = false;

        width = this.icon.getWidth();
        height = this.icon.getHeight();

        x = 432;
        y = 275;

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
                Globals.playServices.playAd();
                Globals.support_button_pressed = true;
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
        if(is_down)
        {
            sprite_batch.setColor(1,1,1,0.5f);
            sprite_batch.draw(icon, x, y);
            sprite_batch.setColor(1,1,1,1);
        }else
        {
            sprite_batch.draw(icon, x, y);
        }
    }
}
