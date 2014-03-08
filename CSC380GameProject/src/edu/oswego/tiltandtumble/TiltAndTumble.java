package edu.oswego.tiltandtumble;

import java.util.ArrayDeque;
import java.util.Deque;

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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import edu.oswego.tiltandtumble.screens.CreditScreen;
import edu.oswego.tiltandtumble.screens.HelpScreen;
import edu.oswego.tiltandtumble.screens.HighScoresScreen;
import edu.oswego.tiltandtumble.screens.LevelScreen;
import edu.oswego.tiltandtumble.screens.MainScreen;
import edu.oswego.tiltandtumble.screens.SettingsScreen;


public class TiltAndTumble extends Game {

	Deque<Screen> screenStack = new ArrayDeque<Screen>(10);

    private MainScreen mainScreen;
    private CreditScreen creditScreen;
    private HelpScreen helpScreen;
    private HighScoresScreen highScoresScreen;
    private SettingsScreen settingsScreen;
    private LevelScreen levelScreen;

	private Skin skin;
	private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;

    private final Settings settings = new Settings();

    @Override
    public void create() {
        settings.setUseDpad(!Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer));
    	batch = new SpriteBatch();
    	stage = new Stage();
    	font = new BitmapFont();
    	skin = new Skin();
        loadSkin();

    	showMainScreen();
    }

    private void loadSkin() {
        // TODO: this should load the skin from a resource file.

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", font);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
        checkBoxStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        checkBoxStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        checkBoxStyle.checked = skin.newDrawable("white", Color.BLUE);
        checkBoxStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        checkBoxStyle.font = skin.getFont("default");
        skin.add("default", checkBoxStyle);
    }

    public void showMainScreen(){
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

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        mainScreen.dispose();
        batch.dispose();
        font.dispose();
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
        // TODO: implement
    }

    @Override
    public void resume() {
        // TODO: implement
    }
}
