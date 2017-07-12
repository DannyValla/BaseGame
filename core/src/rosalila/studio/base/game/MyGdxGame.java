package rosalila.studio.base.game;

import com.badlogic.gdx.Game;

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

		this.setScreen(new rosalila.studio.base.game.Screens.SplashScreen());
	}
	
	@Override
	public void dispose () {

	}
}
