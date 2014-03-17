package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.oswego.tiltandtumble.TiltAndTumble;
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
		PAUSED,
		PLAYING,
		SCORED
	}
	private final BallController	ballController;
	private final WorldPopulator 	worldPopulator;
	private final ScoreDialog scoreDialog;
	PauseDialog pause;
	
	private Level 	level;
	private LevelRenderer 	renderer;
	InputMultiplexer inputMux = new InputMultiplexer();
	
	boolean usingDpad = false;
	
	private State currentState;

	public GameScreen(TiltAndTumble game, int currentLevel){
		super(game);
		ballController = new BallController(!game.getSettings().isUseDpad());
		worldPopulator = new WorldPopulator();
		scoreDialog = new ScoreDialog("Score\n", skin, this);
		pause = new PauseDialog("\nGame Paused\n", skin, this);
		
		this.loadLevel(currentLevel);		
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

	public void loadDpad(){

		
		Texture upTexture = new Texture(Gdx.files.internal("data/UpArrow.png"));
		Texture rightTexture = new Texture(Gdx.files.internal("data/RightArrow.png"));
		Texture downTexture = new Texture(Gdx.files.internal("data/DownArrow.png"));
		Texture leftTexture = new Texture(Gdx.files.internal("data/LeftArrow.png"));
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

	public void loadHUD(int currentLevel){
		//its not pretty but will fix that when timer and score are done
		WindowStyle hudStyle = new WindowStyle();
		hudStyle.background = skin.newDrawable("defaultTexture", Color.DARK_GRAY);
		hudStyle.titleFont = skin.getFont("default");
		hudStyle.titleFontColor = Color.WHITE;
		Window window = new Window("",hudStyle);
		window.setHeight(30);
		
		window.setPosition(0, stage.getHeight());
		window.setWidth(stage.getWidth());
		stage.addActor(window);
		
		HorizontalGroup hud = new HorizontalGroup();
		hud.setSpacing(63f);
		hud.setFillParent(true);
		window.addActor(hud);
		
		Label levelDisplay = new Label("LEVEL " + level.getLevelNumber(), skin );
		hud.addActor(levelDisplay);
		
		Label scoreDisplay = new Label("SCORE: TODO", skin );
		hud.addActor(scoreDisplay);
		
		Label timerDisplay = new Label("TIMER: TODO", skin );
		hud.addActor(timerDisplay);
		
		Texture pauseTexture = new Texture(Gdx.files.internal("data/PauseButton.png"));
		skin.add("pause", pauseTexture);
		Image pauseImage = new Image(skin, "pause");
		hud.addActor(pauseImage);
		pauseImage.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer,
					int button) {
				ballController.pauseBall();
				pause();
				return true;
			}
		});

	}



	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
		inputMux.addProcessor(stage);
		//only have this cause i dont use it but it always comes up
//		if(game.getSettings().isUseDpad()){
//			loadDpad();
//		}
		loadHUD(level.getLevelNumber());
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
	
	public void setStatePause(){
		this.currentState = State.PAUSED;
	}
	
	@Override
	public void pause() {
		this.currentState = State.PAUSED;
		pause.show(stage);
	}

	@Override
	public void resume() {
		ballController.resumeBall();
		this.currentState = State.PLAYING;

	}
}




