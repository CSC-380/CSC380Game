package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.levels.BallController;
import edu.oswego.tiltandtumble.levels.DebugLevelRenderer;
import edu.oswego.tiltandtumble.levels.DefaultLevelRenderer;
import edu.oswego.tiltandtumble.levels.Level;
import edu.oswego.tiltandtumble.levels.LevelRenderer;
import edu.oswego.tiltandtumble.levels.WorldPopulator;

public class GameScreen extends AbstractScreen {

	private final BallController ballController;
	private final WorldPopulator worldPopulator;

	private Level level;
	private LevelRenderer renderer;
	private final InputMultiplexer inputMux = new InputMultiplexer();

	public GameScreen(TiltAndTumble game, int currentLevel) {
		super(game);
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
		renderer = new DefaultLevelRenderer(level, game.getWidth(), game.getHeight());
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
		super.render(delta);

		renderer.render(game.getSpriteBatch(), game.getFont());
		level.update();
	}

	@Override
	public void dispose() {
		super.dispose();
		level.dispose();
		renderer.dispose();
	}
}
