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
 * Created by turupawn on 7/8/17.
 */
public class RightButton extends Actor {

    Texture icon;
    SpriteBatch sprite_batch;
    int x;
    int y;
    int width;
    int height;
    boolean is_down;

    public RightButton(SpriteBatch sprite_batch)
    {
        this.sprite_batch = sprite_batch;
        this.icon = new Texture(Gdx.files.internal("right.png"));
        this.is_down = false;

        width = this.icon.getWidth();
        height = this.icon.getHeight();

        x = 60 + (12-1)%3 * width;
        y = 800-((12-1)/3) * height;

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
                Globals.current_stage_page++;
            }
        });
    }

    @Override
    public void act(float delta)
    {
        if(isOnScreen())
            setTouchable(Touchable.enabled);
        else
            setTouchable(Touchable.disabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(isOnScreen())
        {
            if (is_down) {
                sprite_batch.setColor(1, 1, 1, 0.5f);
            }
            sprite_batch.draw(icon, x, y);
            sprite_batch.setColor(1, 1, 1, 1);
        }
    }

    boolean isOnScreen()
    {
        return Globals.current_stage_page+1 < Globals.total_stages/Globals.stages_per_page;
    }
}
