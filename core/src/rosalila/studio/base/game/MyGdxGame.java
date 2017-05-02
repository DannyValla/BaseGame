package rosalila.studio.base.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {

	public MyGdxGame(PlayServices playServices)
	{
		Globals.playServices = playServices;
	}
	
	@Override
	public void create ()
	{
		Globals.game = this;
		Globals.init();

		this.setScreen(new SplashScreen());
	}
	
	@Override
	public void dispose () {

	}
}
