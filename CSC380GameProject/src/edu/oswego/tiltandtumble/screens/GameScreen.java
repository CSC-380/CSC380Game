package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.levels.BallController;
import edu.oswego.tiltandtumble.levels.DebugLevelRenderer;
import edu.oswego.tiltandtumble.levels.DefaultLevelRenderer;
import edu.oswego.tiltandtumble.levels.Level;
import edu.oswego.tiltandtumble.levels.LevelRenderer;
import edu.oswego.tiltandtumble.levels.WorldPopulator;
import edu.oswego.tiltandtumble.screens.dialogs.ScoreDialog;

public class GameScreen extends AbstractScreen {

	private static enum State {
		PAUSED,
		PLAYING,
		SCORED
	}

	private final WorldPopulator worldPopulator;
	private LevelRenderer renderer;
	private final BallController ballController;

	private final ScoreDialog scoreDialog;
	private State currentState;

	private final InputMultiplexer inputMux = new InputMultiplexer();

	private Level level;

	public GameScreen(TiltAndTumble game, int currentLevel) {
		super(game);
		ballController = new BallController(!game.getSettings().isUseDpad());
		worldPopulator = new WorldPopulator();
		scoreDialog = new ScoreDialog("Score", skin, this);
		loadLevel(currentLevel);
	}

	public void loadLevel(int num) {
		if (level != null) {
			level.dispose();
			level = null;
		}
		if (renderer != null) {
			renderer.dispose();
			renderer = null;
		}
		level = new Level(num, ballController, worldPopulator);
		renderer = new DefaultLevelRenderer(level,game.getWidth(), game.getHeight());
		if (game.getSettings().isDebugRender()) {
			renderer = new DebugLevelRenderer(renderer, ballController);
		}
		currentState = State.PLAYING;

		// TODO: move this to someplace more meaningful as part of issue #19
		level.start();
	}

	public void loadNextLevel() {
		try {
			loadLevel(level.getLevelNumber() + 1);
		}
		catch (Exception ex) {
			// NOTE: we have ran out of levels. this can be done better.
			//       we need a way to know there is no further levels before
			//       we try and load one...
			Gdx.app.log("load level", ex.getMessage());
			game.showPreviousScreen();
		}
	}

	public void loadDpad() {
		Texture upTexture = new Texture(Gdx.files.internal("data/UpArrow.png"));
		Texture rightTexture = new Texture(Gdx.files.internal("data/RightArrow.png"));
		Texture downTexture = new Texture(Gdx.files.internal("data/DownArrow.png"));
		Texture leftTexture = new Texture(Gdx.files.internal("data/LeftArrow.png"));
		Skin skin = game.getSkin();
		skin.add("up", upTexture);
		skin.add("right", rightTexture);
		skin.add("left", leftTexture);
		skin.add("down", downTexture);

		Image image = new Image(skin, "up");
		image.setName("up");
		image.setPosition(image.getWidth(), image.getHeight() * 2);
		image.addListener(ballController);
		stage.addActor(image);

		image = new Image(skin, "right");
		image.setName("right");
		image.setPosition(image.getWidth() * 2, image.getHeight());
		image.addListener(ballController);
		stage.addActor(image);

		image = new Image(skin, "left");
		image.setName("left");
		image.setPosition(0, image.getHeight());
		image.addListener(ballController);
		stage.addActor(image);

		image = new Image(skin, "down");
		image.setName("down");
		image.setPosition(image.getWidth(), 0);
		image.addListener(ballController);
		stage.addActor(image);
	}

	public void loadHUD(){
		//not working but will work on another time
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
		inputMux.addProcessor(stage);

		if(game.getSettings().isUseDpad()){
			loadDpad();
		}
		loadHUD();
	}

	@Override
	protected void preStageRenderHook(float delta) {
		renderer.render(game.getSpriteBatch(), game.getFont());
		if (level.isStarted()) {
			level.update();
		} else if (level.hasFinished()) {
			if (currentState != State.SCORED) {
				scoreDialog.show(stage);
				currentState = State.SCORED;
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if (level != null) {
			level.dispose();
		}
		if (renderer != null) {
			renderer.dispose();
		}
	}
}
