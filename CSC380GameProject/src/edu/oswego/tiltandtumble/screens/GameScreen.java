package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

public class GameScreen extends AbstractScreen implements InputProcessor  {

	private static enum State {
		PAUSED,
		PLAYING,
		SCORED
	}
	private final BallController	ballController;
	private final WorldPopulator 	worldPopulator;
	private final ScoreDialog scoreDialog;
	
	private Level 	level;
	private LevelRenderer 	renderer;
	float width;
	float height;
	private Stage dpadStage;
	boolean usingDpad = false;
	
	private State currentState;

	public GameScreen(TiltAndTumble game, int currentLevel){
		super(game);
		ballController = new BallController(!game.getSettings().isUseDpad());
		worldPopulator = new WorldPopulator();
		scoreDialog = new ScoreDialog("Score\n", skin, this);
		width = game.getWidth();
		height = game.getHeight();
		if(game.getSettings().isUseDpad()){
			loadDpad();
			usingDpad = true;
		}
		loadHUD();
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

		dpadStage = new Stage();
		dpadStage.setViewport(width, height);
		Texture upTexture = new Texture(Gdx.files.internal("data/UpArrow.png"));
		Texture rightTexture = new Texture(Gdx.files.internal("data/RightArrow.png"));
		Texture downTexture = new Texture(Gdx.files.internal("data/DownArrow.png"));
		Texture leftTexture = new Texture(Gdx.files.internal("data/LeftArrow.png"));
		Skin skin = game.getSkin();
		skin.add("up", upTexture);
		skin.add("right", rightTexture);
		skin.add("left", leftTexture);
		skin.add("down", downTexture);

		Image upImage = new Image(skin, "up");
		upImage.setPosition(48, 100);
		dpadStage.addActor(upImage);
		Image rightImage = new Image(skin, "right");
		rightImage.setPosition(95, 55);
		dpadStage.addActor(rightImage);

		Image leftImage = new Image(skin, "left");
		leftImage.setPosition(0, 55);
		dpadStage.addActor(leftImage);

		Image downImage = new Image(skin, "down");
		downImage.setPosition(48, 0);
		dpadStage.addActor(downImage);

	}

	public void loadHUD(){
		//not working but will work on another time
		

	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		preStageRenderHook(delta);
		stage.act(Gdx.graphics.getDeltaTime());
		if(usingDpad){
			dpadStage.act(delta);
		}
		stage.draw();
		if(usingDpad){
			dpadStage.draw();
		}
		postStageRenderHook(delta);
	}


	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
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


	// * InputProcessor methods ***************************//

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT)
			ballController.leftPressed();
		if (keycode == Keys.RIGHT)
			ballController.rightPressed();
		if (keycode == Keys.UP)
			ballController.upPressed();
		if (keycode == Keys.DOWN)
			ballController.downPressed();
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT)
			ballController.leftReleased();
		if (keycode == Keys.RIGHT)
			ballController.rightReleased();
		if (keycode == Keys.UP)
			ballController.upReleased();
		if (keycode == Keys.DOWN)
			ballController.downReleased();
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		//System.out.println("down X: " +x + " Y: "+ y + " pointer: " + pointer + " button: " + button);
		if(!usingDpad){
			return false;
		}
		if(y < 270 && y> 220){
			if(x < 50){
				ballController.leftPressed();
			}
			if(x > 100 && x < 140){
				ballController.rightPressed();
			}
		}
		if(x>50 && x<100){
			if(y>175 && y < 220){
				ballController.upPressed();
			}if( y > 265){
				ballController.downPressed();
			}
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		//System.out.println("upX: " +x + " Y: "+ y + " pointer: " + pointer + " button: " + button);
		if(!usingDpad){
			return false;
		}
		if(y < 270 && y> 220){
			if(x < 50){
				ballController.leftReleased();
			}
			if(x > 100 && x < 140){
				ballController.rightReleased();
			}
		}
		if(x>50 && x<100){
			if(y>175 && y < 220){
				ballController.upReleased();
			}if( y > 265){
				ballController.downReleased();
			}
		}
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}


}




