package edu.oswego.tiltandtumble;

import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.oswego.tiltandtumble.data.HighScores;
import edu.oswego.tiltandtumble.screens.CreditScreen;
import edu.oswego.tiltandtumble.screens.GameScreen;
import edu.oswego.tiltandtumble.screens.HelpScreen;
import edu.oswego.tiltandtumble.screens.HighScoresScreen;
import edu.oswego.tiltandtumble.screens.LevelScreen;
import edu.oswego.tiltandtumble.screens.MainScreen;
import edu.oswego.tiltandtumble.screens.SettingsScreen;


public class TiltAndTumble extends Game {

	// NOTE: older phones do not have Deque interface
	Stack<Screen> screenStack = new Stack<Screen>();

	private MainScreen mainScreen;
	private CreditScreen creditScreen;
	private HelpScreen helpScreen;
	private HighScoresScreen highScoresScreen;
	private SettingsScreen settingsScreen;
	private LevelScreen levelScreen;
	private GameScreen gameScreen;

	private Skin skin;
	private Stage stage;
	private BitmapFont font;
	private SpriteBatch batch;

	private int width;
	private int height;

	private final Settings settings = new Settings();
	private HighScores scores;

	@Override
	public void create() {
		settings.setUseDpad(!Gdx.input
				.isPeripheralAvailable(Peripheral.Accelerometer));
		batch = new SpriteBatch();
		stage = new Stage();

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

		stage.setViewport(width, height, true);
		font = new BitmapFont();
		skin = new Skin();
		loadSkin();
		scores = HighScores.load();
		showMainScreen();
	}

	private void loadSkin() {
		skin.addRegions(new TextureAtlas(Gdx.files.internal("data/ui/tiltandtumble.pack")));
		skin.load(Gdx.files.internal("data/ui/skin.json"));
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
		setScreen(levelScreen);
	}

	public void showGameScreen(int level) {
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		screenStack.push(getScreen());
		gameScreen = new GameScreen(this, level);
		setScreen(gameScreen);
	}

	public void showPreviousScreen() {
		setScreen(screenStack.pop());
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

	@Override
	public void dispose() {
		HighScores.save(scores);
		mainScreen.dispose();
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
		stage.dispose();
		batch.dispose();
		font.dispose();
		skin.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		// NOTE: we probably don't need this
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {


	}
}
