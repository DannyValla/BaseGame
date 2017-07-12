package rosalila.studio.base.game;

/**
 * Created by turupawn on 4/22/17.
 */
public interface PlayServices
{
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement(String achievement_id);
    public void showAchievementsIntent();
    public void submitScore(int highScore);
    public void showAchievement();
    public void showScore();
    public boolean isSignedIn();

    public void playAd();
}