package edu.oswego.tiltandtumble;

import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;

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

	private final int width = 480;
	private final int height = 320;

	private final Settings settings = new Settings();

	@Override
	public void create() {
		settings.setUseDpad(!Gdx.input
				.isPeripheralAvailable(Peripheral.Accelerometer));
		batch = new SpriteBatch();
		stage = new Stage();
		stage.setViewport(width, height, true);
		font = new BitmapFont();
		skin = new Skin();
		loadSkin();
	
		showMainScreen();
	}

	private void loadSkin() {
		// TODO: this should load the skin from a resource file.

		// Generate a 1x1 white texture and store it in the skin named "defaultTexture".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("defaultTexture", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		skin.add("default", font);

		// Configure a TextButtonStyle and name it "default". Skin resources are
		// stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("defaultTexture", Color.BLACK);
		textButtonStyle.down = skin.newDrawable("defaultTexture", Color.BLACK);
		textButtonStyle.checked = skin.newDrawable("defaultTexture", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("defaultTexture", Color.BLACK);
		textButtonStyle.font = skin.getFont("default");
		// textButtonStyle.font.setScale(5);
		skin.add("default", textButtonStyle);
		CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
		checkBoxStyle.up = skin.newDrawable("defaultTexture", Color.BLACK);
		checkBoxStyle.down = skin.newDrawable("defaultTexture", Color.BLACK);
		checkBoxStyle.checked = skin.newDrawable("defaultTexture", Color.BLUE);
		checkBoxStyle.over = skin.newDrawable("defaultTexture", Color.BLACK);
		checkBoxStyle.font = skin.getFont("default");
		skin.add("default", checkBoxStyle);
		WindowStyle windowStyle = new WindowStyle();
		//windowStyle.stageBackground = skin.newDrawable("defaultTexture",
		//		new Color(Color.CYAN.r, Color.RED.g, Color.GREEN.b, 0.5f));
		windowStyle.background = skin.newDrawable("defaultTexture", Color.LIGHT_GRAY);
		windowStyle.titleFont = skin.getFont("default");
		windowStyle.titleFontColor = Color.WHITE;
		skin.add("default", windowStyle);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.background = skin.newDrawable("defaultTexture", Color.CLEAR);
		labelStyle.font = skin.getFont("default");
		labelStyle.fontColor = Color.WHITE;
		
		skin.add("default", labelStyle);
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

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	@Override
	public void dispose() {
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
