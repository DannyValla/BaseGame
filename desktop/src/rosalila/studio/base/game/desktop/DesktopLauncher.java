package rosalila.studio.base.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import rosalila.studio.base.game.MyGdxGame;
import rosalila.studio.base.game.PlayServices;

public class DesktopLauncher implements PlayServices {
	@Override
	public void signIn() {
		System.out.print("signIn");
	}

	@Override
	public void signOut() {
		System.out.print("signOut");
	}

	@Override
	public void rateGame() {
		System.out.print("rateGame");
	}

	@Override
	public void unlockAchievement(String achievement_id) {
		System.out.print("unlockAchievement " + achievement_id);
	}

	@Override
	public void showAchievementsIntent() {
		System.out.print("showAchievementsIntent");
	}

	@Override
	public void submitScore(int highScore) {
		System.out.print("submitScore");
	}

	@Override
	public void showAchievement() {
		System.out.print("showAchievement");
	}

	@Override
	public void showScore() {
		System.out.print("showScore");
	}

	@Override
	public boolean isSignedIn() {
		System.out.print("isSignedIn");
		return false;
	}

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=540;
		config.height=960;
		new LwjglApplication(new MyGdxGame(new DesktopLauncher()), config);
	}
}
