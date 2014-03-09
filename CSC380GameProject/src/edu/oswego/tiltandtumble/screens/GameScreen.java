package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.levels.BallController;
import edu.oswego.tiltandtumble.levels.DebugLevelRenderer;
import edu.oswego.tiltandtumble.levels.DefaultLevelRenderer;
import edu.oswego.tiltandtumble.levels.Level;
import edu.oswego.tiltandtumble.levels.LevelRenderer;
import edu.oswego.tiltandtumble.levels.WorldPopulator;

public class GameScreen implements Screen /*, InputProcessor */ {

	private final BallController ballController;
	private final WorldPopulator worldPopulator;
	private final TiltAndTumble game;

	private Level level;
	private LevelRenderer renderer;
	private final InputMultiplexer inputMux = new InputMultiplexer();

	private final int width = 480;
	private final int height = 320;

	public GameScreen(TiltAndTumble game, int currentLevel) {
		this.game = game;
		ballController = new BallController(!game.getSettings().isUseDpad());
		worldPopulator = new WorldPopulator();

		loadLevel(currentLevel);
	}

	public void loadLevel(int num) {
		if (level != null) {
			inputMux.removeProcessor(level.getInputProcessor());
			level.dispose();
			renderer.dispose();
		}
		level = new Level(num, ballController, worldPopulator);
		inputMux.addProcessor(level.getInputProcessor());
		renderer = new DefaultLevelRenderer(level, width, height);
		if (game.getSettings().isDebugRender()) {
			renderer = new DebugLevelRenderer(renderer, ballController);
		}
	}

	public void loadNextLevel() {
		loadLevel(level.getLevelNumber() + 1);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		renderer.render(game.getSpriteBatch(), game.getFont());
		level.update();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
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
		level.dispose();
		renderer.dispose();
	}
}

