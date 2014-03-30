package edu.oswego.tiltandtumble.screens;

import java.util.ArrayList;
import java.util.List;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.data.Score;
import edu.oswego.tiltandtumble.levels.AudioManager;
import edu.oswego.tiltandtumble.levels.BallController;
import edu.oswego.tiltandtumble.levels.DebugLevelRenderer;
import edu.oswego.tiltandtumble.levels.DefaultLevelRenderer;
import edu.oswego.tiltandtumble.levels.Level;
import edu.oswego.tiltandtumble.levels.LevelRenderer;
import edu.oswego.tiltandtumble.levels.WorldPopulator;
import edu.oswego.tiltandtumble.screens.dialogs.PauseDialog;
import edu.oswego.tiltandtumble.screens.dialogs.ScoreDialog;

public class GameScreen extends AbstractScreen {

	private static enum State {
		WAITING,
		STARTING,
		PAUSED,
		PLAYING,
		SCORED
	}

	private static final float MAX_COUNT = 4;

	private final BallController ballController;
	private final WorldPopulator worldPopulator;

	private Level level;
	private LevelRenderer renderer;
	private AudioManager audio;
	private final Window startPauseWindow = new Window("", skin);
	InputMultiplexer inputMux = new InputMultiplexer();

	boolean usingDpad = false;
	private final List<Score> scores = new ArrayList<Score>();
	private State currentState;

	private final Label scoreDisplay;
	private final Label timerDisplay;

	private Dialog pauseDialog;
	private float countdownTime;

	public GameScreen(TiltAndTumble game, int currentLevel) {
		super(game);
		ballController = new BallController(!game.getSettings().isUseDpad());
		worldPopulator = new WorldPopulator();

		this.loadLevel(currentLevel);
		scoreDisplay = new Label(String.valueOf(level.getScore().getPoints()), skin, "hud-values");
		timerDisplay = new Label(level.getScore().getFormattedTime(), skin, "hud-values");
	}

	public void loadLevel(int num) {
		currentState = State.WAITING;
		if (level != null) {
			level.dispose();
			level = null;
		}
		if (renderer != null) {
			renderer.dispose();
			renderer = null;
		}
		if (audio != null) {
			game.getSettings().removeObserver(audio);
			audio.dispose();
			audio = null;
		}
		level = new Level(num, ballController, worldPopulator);
		renderer = new DefaultLevelRenderer(level,game.getWidth(), game.getHeight(), game.getSpriteBatch());
		if (game.getSettings().isDebugRender()) {
			renderer = new DebugLevelRenderer(renderer, ballController);
		}
		audio = new AudioManager(
				level,
				game.getSettings().isMusicOn(),
				game.getSettings().isSoundEffectOn());
		game.getSettings().addObserver(audio);
		showStartPause();
	}

	public boolean hasMoreLevels() {
		return Gdx.files.internal("data/level" + (level.getLevelNumber() + 1) + ".tmx").exists();
	}

	public void loadNextLevel() {
		if (hasMoreLevels() && !level.isFailed()) {
			loadLevel(level.getLevelNumber() + 1);
		}
		else {
			game.showPreviousScreen();
		}
	}

	public void loadDpad(){
		Image image = new Image(skin, "UpArrow");
		image.setName("up");
		image.setPosition(image.getWidth(), image.getHeight() * 2);
		image.addListener(ballController);
		stage.addActor(image);

		image = new Image(skin, "RightArrow");
		image.setName("right");
		image.setPosition(image.getWidth() * 2, image.getHeight());
		image.addListener(ballController);
		stage.addActor(image);

		image = new Image(skin, "LeftArrow");
		image.setName("left");
		image.setPosition(0, image.getHeight());
		image.addListener(ballController);
		stage.addActor(image);

		image = new Image(skin, "DownArrow");
		image.setName("down");
		image.setPosition(image.getWidth(), 0);
		image.addListener(ballController);
		stage.addActor(image);

	}

	public Level getCurrentLevel() {
		return level;
	}

	public List<Score> getScores() {
		return scores;
	}

	public void loadHUD() {
		Window window = new Window("", skin, "hud");
		window.setHeight(32);

		window.setPosition(0, stage.getHeight());
		window.setWidth(stage.getWidth());
		stage.addActor(window);

		window.row().uniform().expandX();
		window.add("Level: ").right().padLeft(10);
		window.add(String.valueOf(level.getLevelNumber()), "hud-values").left();
		window.add("Score: ").right();
		window.add(scoreDisplay).left();
		window.add("Time: ").right();
		window.add(timerDisplay).left();

		Image pauseImage = new Image(skin, "PauseButton2");
		window.add(pauseImage).right().fill(false).padRight(10);
		pauseImage.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (currentState == State.PLAYING){
					pause();
				} else if(currentState == State.PAUSED){
					resume();
				}
				return true;
			}
		});
	}

	private void showStartPause() {
		final Window window = new Window("", skin, "dialog");
		stage.addActor(window);
		TextButton button = new TextButton("Start", skin);
		window.add(button);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameScreen.this.startCountDown();
				window.remove();
			}
		});
		window.setClip(false);
		window.setSize(0, 0);
		window.setPosition(stage.getWidth() / 2 , stage.getHeight() / 2);
	}

	private void startCountDown() {
		if (currentState != State.WAITING) return;
		currentState = State.STARTING;
		countdownTime = 0;
	}

	private void showCountDown(float delta) {
		countdownTime += delta;
		// TODO: do some rendering
		//       keep track of a timer and show the correct number for the time
		//       once time is up, call endCountDown();
		countdownTime += delta;
		int count = (int) Math.floor(MAX_COUNT - countdownTime);
			
		if(startPauseWindow.hasParent() == false) {
			stage.addActor(startPauseWindow);
			startPauseWindow.setSize(0, 0);
			startPauseWindow.setPosition(stage.getWidth() / 2 , stage.getHeight() / 2);
		}
		startPauseWindow.clear();
		startPauseWindow.add((String.valueOf(count)));
		if (countdownTime >MAX_COUNT) {
			startPauseWindow.remove();
			endCountDown();
		}
	}

	private void endCountDown() {
		if (currentState != State.STARTING) return;
		currentState = State.PLAYING;
		ballController.resetBall();
		ballController.resume();
		level.start();
		audio.start();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
		inputMux.addProcessor(stage);
		if(game.getSettings().isUseDpad()){
			loadDpad();
		}
		loadHUD();
		if (currentState == State.PAUSED) {
			pauseDialog = new PauseDialog("Paused", skin, this, game).show(stage);
		} else {
			showStartPause();
		}
	}

	@Override
	protected void preStageRenderHook(float delta) {
		renderer.render(delta, game.getSpriteBatch(), game.getFont());
		if (currentState == State.STARTING) {
			showCountDown(delta);
		} else {
			if (level.isStarted()) {
				level.update(delta);
			} else if (level.hasFinished()) {
				if (currentState != State.SCORED) {
					audio.pause();
					scores.add(level.getScore());
					new ScoreDialog("Score", skin, game, this).show(stage);
					currentState = State.SCORED;
				}
			}

			scoreDisplay.setText(String.valueOf(level.getScore().getPoints()));
			timerDisplay.setText(level.getScore().getFormattedTime());
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
		if (audio != null) {
			audio.dispose();
		}
	}

	@Override
	public void pause() {
		if (currentState != State.PLAYING) return;
		pauseDialog = new PauseDialog("Paused", skin, this, game).show(stage);
		currentState = State.PAUSED;
		ballController.pause();
		audio.pause();
		level.pause();
	}

	@Override
	public void resume() {
		if (currentState != State.PAUSED) return;
		// isVisible seems to always return true, not sure why...
		// make sure the dialog goes away, this can be called from the system
		// level rather than direct user interaction so we want to make sure
		// the game does not start playing before the window goes away.
		if (pauseDialog != null && pauseDialog.isVisible()) {
			pauseDialog.hide();
			pauseDialog = null;
		}
		this.currentState = State.PLAYING;
		ballController.resume();
		audio.start();
		level.resume();
	}
}
