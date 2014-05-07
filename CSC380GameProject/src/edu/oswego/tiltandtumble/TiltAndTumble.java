package edu.oswego.tiltandtumble;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.data.HighScores;
import edu.oswego.tiltandtumble.screens.ChallengeScreen;
import edu.oswego.tiltandtumble.screens.CreditScreen;
import edu.oswego.tiltandtumble.screens.GameScreen;
import edu.oswego.tiltandtumble.screens.HelpScreen;
import edu.oswego.tiltandtumble.screens.HighScoresScreen;
import edu.oswego.tiltandtumble.screens.LevelScreen;
import edu.oswego.tiltandtumble.screens.MainScreen;
import edu.oswego.tiltandtumble.screens.NetworkingLevelScreen;
import edu.oswego.tiltandtumble.screens.SettingsScreen;
import edu.oswego.tiltandtumble.settings.Settings;
import edu.oswego.tiltandtumble.settings.Settings.Setting;
import edu.oswego.tiltandtumble.settings.SettingsObserver;
import edu.oswego.tiltandtumble.settings.SettingsUpdate;


public class TiltAndTumble extends Game implements SettingsObserver {

	// NOTE: older phones do not have Deque interface
	Stack<Screen> screenStack = new Stack<Screen>();
	private boolean challengeMode = false;
	private boolean challengeAcceptMode = false;
	private String name;
	
	private final List<String> levels = new ArrayList<String>();
	{
		levels.add("Tutorial3.tmx");
		levels.add("level4.tmx");
		levels.add("level5.tmx");
		levels.add("Tutorial1.tmx");
		
		levels.add("Tutorial2.tmx");
		levels.add("level3.tmx");
		levels.add("level2.tmx");
		levels.add("level1.tmx");
	}

	private MainScreen mainScreen;
	private CreditScreen creditScreen;
	private HelpScreen helpScreen;
	private HighScoresScreen highScoresScreen;
	private SettingsScreen settingsScreen;
	private LevelScreen levelScreen;
	private GameScreen gameScreen;
	private ChallengeScreen challengeScreen;
	private NetworkingLevelScreen networkingLevelScreen;

	private AssetManager assetManager;
	private Skin skin;
	private Stage stage;
	private BitmapFont font;
	private SpriteBatch batch;

	private int width;
	private int height;

	private Settings settings;
	private HighScores scores;
	
	private Session session;

	private boolean playMusic;
	private Music music;

	@Override
	public void create() {
		Texture.setEnforcePotImages(true);

		assetManager = new AssetManager();

		settings = new Settings();
		settings.addObserver(this);

		batch = new SpriteBatch();

		// this will set the view port to the screen size, which will cause
		// things to look big on a low resolution screen and look small on a
		// high resolution screen. we then probably have to scale the ui
		// up or down to make things easier to see. If we just hard code
		// a size then the game engine will just scale the entire render view
		// to the correct size.
		//
		// width = Gdx.graphics.getWidth();
		// height = Gdx.graphics.getHeight();

		width = 480;
		height = 320;

		stage = new Stage(width, height, true, batch);

		font = new BitmapFont();
		loadSkin();
		scores = HighScores.load();
		playMusic = settings.isMusicOn();
		String musicFile = "data/music/GameMenuMusic.mp3";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		music = assetManager.get(musicFile, Music.class);
		this.playMusic();
		String buttonFile = "data/soundfx/button-8.ogg";
		if (!assetManager.isLoaded(buttonFile)) {
			assetManager.load(buttonFile, Sound.class);
			assetManager.finishLoading();
		}
		musicFile = "data/soundfx/number-zero.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		
        musicFile = "data/soundfx/number-one.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		
		musicFile = "data/soundfx/number-two.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		
		musicFile = "data/soundfx/number-three.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		
		
		showMainScreen();
	}

	private void loadSkin() {
		assetManager.load("data/ui/skin.json", Skin.class,
				new SkinLoader.SkinParameter("data/ui/tiltandtumble.pack"));
		assetManager.finishLoading();

		skin = assetManager.get("data/ui/skin.json", Skin.class);
	}

	public void showMainScreen() {
		if (mainScreen == null) {
			mainScreen = new MainScreen(this);
		}
		if (getScreen() != null) {
			screenStack.push(getScreen());
		}
		setScreen(mainScreen);
	}

	public void showCreditScreen() {
		if (creditScreen == null) {
			creditScreen = new CreditScreen(this);
		}
		screenStack.push(getScreen());
		setScreen(creditScreen);
	}
	
	public void showNetworkingLevelScreen(String name){
		if (networkingLevelScreen == null) {
			networkingLevelScreen = new NetworkingLevelScreen(this);
		}
		this.name = name;
		
		screenStack.push(getScreen());
		setScreen(networkingLevelScreen);
		
	}
	
	public String getName(){
		return name;
	}

	public void showHelpScreen() {
		if (helpScreen == null) {
			helpScreen = new HelpScreen(this);
		}
		screenStack.push(getScreen());
		setScreen(helpScreen);
	}

	public void showHighScoresScreen() {
		if (highScoresScreen == null) {
			highScoresScreen = new HighScoresScreen(this);
		}
		screenStack.push(getScreen());
		setScreen(highScoresScreen);
	}

	public void showSettingsScreen() {
		if (settingsScreen == null) {
			settingsScreen = new SettingsScreen(this);
		}
		screenStack.push(getScreen());
		setScreen(settingsScreen);
	}

	public void showLevelScreen() {
		if (levelScreen == null) {
			levelScreen = new LevelScreen(this);
		}
		screenStack.push(getScreen());
		//this.playMusic();
		setScreen(levelScreen);
	}
	
	public void showChallengeScreen() {
		if (challengeScreen == null) {
			challengeScreen = new ChallengeScreen(this);
		}
		screenStack.push(getScreen());
		setScreen(challengeScreen);
	}

	public void showGameScreen(int level, GameScreen.Mode mode) {
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		screenStack.push(getScreen());
		gameScreen = new GameScreen(this, level, mode);
		//this.endMusic();
		setScreen(gameScreen);
	}

	public void showPreviousScreen() {
		if(screenStack.peek() == mainScreen){
			this.challengeAcceptMode = false;
			this.challengeMode = false;
		}
		if(screenStack.peek() != gameScreen){
		this.playMusic();
		}
		setScreen(screenStack.pop());
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public Skin getSkin() {
		return skin;
	}

	public Stage getStage() {
		return stage;
	}

	public BitmapFont getFont() {
		return font;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public Settings getSettings() {
		return settings;
	}

	public HighScores getHighScores() {
		return scores;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	public boolean isChallengeMode(){
		return challengeMode;
	}
	
	
	public void setChallengeMode(boolean x){
		challengeMode = x;
	}
	//
	public boolean isChallengeAcceptMode(){
		return challengeAcceptMode;
	}
	
	public void setChallengeAcceptMode(boolean x){
		challengeAcceptMode = x;
	}
	
	public Session getSession() {
		if(session != null) {
			return session;
		}
		Cluster cluster = Cluster.builder().addContactPoint("129.3.20.26").withPort(2715).build();
        session = cluster.connect();
		//session.execute("DROP KEYSPACE challengeMap");
		//session.execute("CREATE KEYSPACE challengeMap "
		//		+ "WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };");

		session.execute("USE challenges;");
		//session.execute("CREATE TABLE users (" + "username text PRIMARY KEY, "
		//		+ "highscore int, " + "pathx map<int, float>, "
		//		+ "pathy map<int, float>);");
		return session;
	}
	

	public List<String> getLevels() {
		return levels;
	}

	public void playMusic() {
		if (playMusic && music != null) {
			music.setVolume(1f);
			music.play();
			music.setLooping(true);
		}
	}

	public void endMusic() {
		if (music != null) {
			float vol = music.getVolume();
			while(vol > 0f){
				vol = (float) (vol - (0.02));
				if(vol < 0f){
					vol = 0f;
				}
				music.setVolume(vol);
			}
			music.stop();
		}
	}



	@Override
	public void dispose() {
		HighScores.save(scores);
		mainScreen.dispose();
		music.dispose();
		if (creditScreen != null) {
			creditScreen.dispose();
		}
		if (helpScreen != null) {
			helpScreen.dispose();
		}
		if (highScoresScreen != null) {
			highScoresScreen.dispose();
		}
		if (settingsScreen != null) {
			settingsScreen.dispose();
		}
		if (levelScreen != null) {
			levelScreen.dispose();
		}
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		if (challengeScreen != null) {
			challengeScreen.dispose();
		}
		if (networkingLevelScreen != null) {
			networkingLevelScreen.dispose();
		}
		stage.dispose();
		batch.dispose();
		font.dispose();
		assetManager.dispose();
	}

	@Override
	public void handleSettingsChangeUpdate(SettingsUpdate update) {
		if (update.getSetting() == Setting.MUSIC) {
			playMusic = update.getValue();
			if (playMusic) {
				playMusic();
			} else {
				endMusic();
			}
		}
	}
}
