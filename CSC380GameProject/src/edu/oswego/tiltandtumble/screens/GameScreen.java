package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.levels.Level;

public class GameScreen implements Screen /*, InputProcessor */ {

	//private WorldPopulator worldPopulator;
	//private WorldRenderer renderer;
	//private BallController controller;
	TiltAndTumble game;

	private Level level;


	private final int width = 480;
	private final int height = 320;

	public GameScreen(TiltAndTumble game, int currentLevel){
        this.game = game;
		loadLevel(currentLevel);
	}

	public void loadLevel(int num) {
	    level = new Level(num, game.getSettings());
	}

	public void loadNextLevel() {
	    loadLevel(level.getLevelNumber() + 1);
	}

	@Override
	public void show() {
//		Gdx.input.setInputProcessor(this);
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		level.render(game.getSpriteBatch(), game.getFont());
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
//		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO: implement
	}

	@Override
	public void resume() {
		// TODO: implement
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}

	// * InputProcessor methods ***************************//
/*
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT)
			controller.leftPressed();
		if (keycode == Keys.RIGHT)
			controller.rightPressed();
		if (keycode == Keys.UP)
			controller.upPressed();
		if (keycode == Keys.DOWN)
			controller.downPressed();
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT)
			controller.leftReleased();
		if (keycode == Keys.RIGHT)
			controller.rightReleased();
		if (keycode == Keys.UP)
			controller.upReleased();
		if (keycode == Keys.DOWN)
			controller.downReleased();
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (x < width / 2 && y > height / 2) {
			controller.leftPressed();
		}
		if (x > width / 2 && y > height / 2) {
			controller.rightPressed();
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (x < width / 2 && y > height / 2) {
			controller.leftReleased();
		}
		if (x > width / 2 && y > height / 2) {
			controller.rightReleased();
		}
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
*/
}

