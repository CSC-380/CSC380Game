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
	private static final float MAX_COUNT = 4;

	private final BallController ballController;
	private final WorldPopulator worldPopulator;

	private Level level;
	private LevelRenderer renderer;
	private AudioManager audio;
	private final Window countdownDisplay;
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

		countdownDisplay = new Window("", skin, "countdown");
	}

	public void loadLevel(int num) {
		changeState(State.WAITING);
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

	private void loadDpad() {
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

	private void loadHUD() {
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
				currentState.togglePause(GameScreen.this);
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
				currentState.start(GameScreen.this);
				window.remove();
			}
		});
		window.setClip(false);
		window.setSize(0, 0);
		window.setPosition(stage.getWidth() / 2 , stage.getHeight() / 2);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
		inputMux.addProcessor(stage);
		if (game.getSettings().isUseDpad()){
			loadDpad();
		}
		loadHUD();
		currentState.show(this);
	}

	@Override
	protected void preStageRenderHook(float delta) {
		renderer.render(delta, game.getSpriteBatch(), game.getFont());
		currentState.render(this, delta);
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
		currentState.pause(this);
	}

	@Override
	public void resume() {
		currentState.resume(this);
	}

	private void changeState(State state) {
		currentState = state;
	}

	private static enum State {
		WAITING {
			@Override
			public void show(GameScreen s) {
				s.showStartPause();
			}

			@Override
			public void start(GameScreen s) {
				s.changeState(State.STARTING);
			}
		},
		STARTING {
			@Override
			public void render(GameScreen s, float delta) {
				s.countdownTime += delta;
				int count = (int)Math.floor(MAX_COUNT - s.countdownTime);

				if (s.countdownDisplay.hasParent() == false) {
					s.stage.addActor(s.countdownDisplay);
					s.countdownDisplay.setSize(s.stage.getWidth(), s.stage.getHeight());
					s.countdownDisplay.setPosition(s.stage.getWidth() / 2 , s.stage.getHeight() / 2);
					s.countdownDisplay.setClip(false);
				}
				s.countdownDisplay.clear();
				s.countdownDisplay.add(String.valueOf(count), "countdown");
				if (s.countdownTime > MAX_COUNT) {
					s.countdownDisplay.remove();
					s.ballController.resetBall();
					s.ballController.resume();
					s.level.start();
					s.audio.start();
					s.changeState(State.PLAYING);
				}
			}
		},
		PAUSED {
			@Override
			public void resume(GameScreen s) {
				// isVisible seems to always return true, not sure why...
				// make sure the dialog goes away, this can be called from the system
				// level rather than direct user interaction so we want to make sure
				// the game does not start playing before the window goes away.
				if (s.pauseDialog != null && s.pauseDialog.isVisible()) {
					s.pauseDialog.hide();
					s.pauseDialog = null;
				}
				s.ballController.resume();
				s.audio.start();
				s.level.resume();
				s.changeState(State.PLAYING);
			}

			@Override
			public void show(GameScreen s) {
				s.pauseDialog = new PauseDialog("Paused", s.skin, s, s.game).show(s.stage);
			}

			@Override
			public void togglePause(GameScreen s) {
				resume(s);
			}
		},
		PLAYING {
			@Override
			public void render(GameScreen s, float delta) {
				if (s.level.hasFinished()) {
					s.audio.pause();
					s.scores.add(s.level.getScore());
					new ScoreDialog("Score", s.skin, s.game, s).show(s.stage);
					s.changeState(State.SCORED);
				}
				else {
					s.level.update(delta);
				}

				s.scoreDisplay.setText(String.valueOf(s.level.getScore().getPoints()));
				s.timerDisplay.setText(s.level.getScore().getFormattedTime());
			}

			@Override
			public void pause(GameScreen s) {
				s.pauseDialog = new PauseDialog("Paused", s.skin, s, s.game).show(s.stage);
				s.ballController.pause();
				s.audio.pause();
				s.level.pause();
				s.changeState(State.PAUSED);
			}

			@Override
			public void togglePause(GameScreen s) {
				pause(s);
			}
		},
		SCORED;

		public void start(GameScreen s) {}
		public void togglePause(GameScreen s) {}
		public void pause(GameScreen s) {}
		public void resume(GameScreen s) {}
		public void show(GameScreen s) {}
		public void render(GameScreen s, float delta) {}
	}
}
