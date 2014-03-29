package edu.oswego.tiltandtumble.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
import edu.oswego.tiltandtumble.screens.dialogs.StartPauseDialog;

public class GameScreen extends AbstractScreen {

	private static enum State {
		PAUSED,
		PLAYING,
		SCORED
	}

	private final BallController ballController;
	private final WorldPopulator worldPopulator;
	private final PauseDialog pauseDialog;
	private final StartPauseDialog startPauseDialog;

	private Level level;
	private LevelRenderer renderer;
	private AudioManager audio;
	InputMultiplexer inputMux = new InputMultiplexer();

	boolean usingDpad = false;
	boolean firstStartPause = true;
	private final List<Score> scores = new ArrayList<Score>();
	private State currentState;

	Label scoreDisplay;
	Label timerDisplay;

	public GameScreen(TiltAndTumble game, int currentLevel) {
		super(game);
		ballController = new BallController(!game.getSettings().isUseDpad());
		worldPopulator = new WorldPopulator();
		pauseDialog = new PauseDialog("Paused", skin, this);
		startPauseDialog = new StartPauseDialog("", skin, this);

		this.loadLevel(currentLevel);
		scoreDisplay = new Label(String.valueOf(level.getScore().getPoints()), skin, "hud-values");
		timerDisplay = new Label(level.getScore().getFormattedTime(), skin, "hud-values");
	}

	public void loadLevel(int num) {
		ballController.resetBall();
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
		renderer = new DefaultLevelRenderer(level,game.getWidth(), game.getHeight());
		if (game.getSettings().isDebugRender()) {
			renderer = new DebugLevelRenderer(renderer, ballController);
		}
		audio = new AudioManager(
				level,
				game.getSettings().isMusicOn(),
				game.getSettings().isSoundEffectOn());
		game.getSettings().addObserver(audio);
	}

	public boolean hasMoreLevels() {
		return Gdx.files.internal("data/level" + (level.getLevelNumber() + 1) + ".tmx").exists();
	}

	public void loadNextLevel() {
		startPause();
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
		//its not pretty but will fix that when timer and score are done
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

	public void startPause() {
		startPauseDialog.show(stage);
		ballController.pause();
		currentState = State.PAUSED;
		Button start = new TextButton("start", skin);
		if(firstStartPause==true) {
			startPauseDialog.button(start);
			firstStartPause=false;
		}
		start.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer,
					int button) {
				currentState = State.PLAYING;
				audio.start();
				level.start();
				return true;
			}
		});
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
		inputMux.addProcessor(stage);
		if(game.getSettings().isUseDpad()){
			loadDpad();
		}
		loadHUD();
		startPause();
	}

	@Override
	protected void preStageRenderHook(float delta) {
		renderer.render(delta, game.getSpriteBatch(), game.getFont());
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
		pauseDialog.show(stage);
		currentState = State.PAUSED;
		ballController.pause();
		audio.pause();
		level.pause();
	}

	@Override
	public void resume() {
		this.currentState = State.PLAYING;
		ballController.resume();
		audio.start();
		level.resume();
	}
}
