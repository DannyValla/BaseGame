package rosalila.studio.base.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by turupawn on 4/23/17.
 */
public class Player {

    static float WIDTH;
    static float HEIGHT;

    Texture texture;

    final Vector2 position = new Vector2();

    ArrayList<Vector2> last_positions;

    boolean last_frame_touch_up = true;
    double last_frame_swipe_x = 0;

    double destination_x;
    double current_x;

    public Player()
    {
        texture = new Texture("player.png");

        this.position.set(7*48, 0);
        this.destination_x = this.position.x;
        this.current_x = this.position.x;

        last_positions = new ArrayList<Vector2>();

        for(int i=0;i<5;i++)
        {
            last_positions.add(new Vector2(position.x,position.y));
        }

        this.WIDTH = 0.75f;
        this.HEIGHT = 0.75f;
    }

    public void update(float deltaTime)
    {
        if(Globals.game_state.equals("running") || Globals.game_state.equals("won"))
        {
            last_positions.add(new Vector2(position.x, position.y));
            last_positions.remove(0);
        }

        if(Globals.game_state.equals("won") || Globals.game_state.equals("lost"))
            return;

        if(Gdx.input.isTouched())
        {
            last_frame_swipe_x = Gdx.input.getDeltaX();
            last_frame_touch_up = false;
        }
        else if(!last_frame_touch_up)
        {
            last_frame_touch_up = true;

            if(last_frame_swipe_x >= 1)
            {
                if(this.position.x < 48*9 && Globals.game_state.equals("tutorial"))
                {
                    Globals.sounds.get("win").play();
                    Globals.game_state = "running";
                }

                this.destination_x += 6*48;
                if(this.destination_x > 13*48)
                {
                    this.destination_x = 13 * 48;
                }
            }else if(last_frame_swipe_x <= -1)
            {
                if(this.position.x > 48*3 && Globals.game_state.equals("tutorial"))
                {
                    Globals.sounds.get("win").play();
                    Globals.game_state = "running";
                }

                this.destination_x -= 6*48;
                if(this.destination_x < 1*48)
                {
                    this.destination_x = 1 * 48;
                }
            }
        }
        //this.current_x = this.destination_x;
        if(Globals.game_state.equals("running"))
        {
            if (this.current_x < this.destination_x)
                this.current_x += 48 / 3;
            if (this.current_x > this.destination_x)
                this.current_x -= 48 / 3;
            this.position.x = (int) this.current_x;
        }
    }

    public void render(float deltaTime, OrthogonalTiledMapRenderer renderer)
    {
        Batch batch = renderer.getBatch();
        batch.begin();
        float alpha = 0.5f;
        float separation_delta = 3;
        float separation = separation_delta;
        for(int i=last_positions.size()-1;i>=0;i--)
        {
            batch.setColor(1, 1, 1, alpha);
            if(!Globals.game_state.equals("lost"))
            {
                batch.draw(texture, last_positions.get(i).x + separation, last_positions.get(i).y);
                batch.draw(texture, last_positions.get(i).x - separation, last_positions.get(i).y);
            }else
            {
                batch.draw(texture, this.position.x + (float)(Math.random()*100) - 50, this.position.y + (float)(Math.random()*100) - 50);
            }
            alpha/=2;
            separation+=separation_delta;
        }
        batch.setColor(1,1,1,1);
        batch.draw(texture, this.position.x, this.position.y);
        batch.end();
    }
}
