package rosalila.studio.base.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import javax.sound.midi.SysexMessage;

/**
 * Created by turupawn on 4/21/17.
 */
public class StageButtonActor extends Actor {

    Texture icon, icon_complete;
    int stage_number;
    SpriteBatch sprite_batch;
    int x;
    int y;
    int width;
    int height;
    boolean is_down;

    public StageButtonActor(final int stage_number, SpriteBatch sprite_batch)
    {
        this.stage_number = stage_number;
        this.sprite_batch = sprite_batch;
        this.icon = new Texture(Gdx.files.internal("level_icons/"+stage_number+".png"));
        this.icon_complete = new Texture(Gdx.files.internal("level_icons_complete/"+stage_number+".png"));
        this.is_down = false;

        width = this.icon.getWidth();
        height = this.icon.getHeight();

        x = 60 + (stage_number-1)%3 * width;
        y = 800-((stage_number-1)/3)%3 * height;

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
        if(isOnScreen())
            setTouchable(Touchable.enabled);
        else
            setTouchable(Touchable.disabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(isOnScreen()) {
            if (is_down) {
                sprite_batch.setColor(1, 1, 1, 0.5f);
            }
            if (Globals.preferences.getBoolean("level" + stage_number + "_complete", false))
                sprite_batch.draw(icon_complete, x, y);
            else
                sprite_batch.draw(icon, x, y);
            sprite_batch.setColor(1, 1, 1, 1);
        }
    }

    boolean isOnScreen()
    {
        return stage_number>Globals.current_stage_page*Globals.stages_per_page && stage_number<=(Globals.current_stage_page+1)*Globals.stages_per_page;
    }
}
