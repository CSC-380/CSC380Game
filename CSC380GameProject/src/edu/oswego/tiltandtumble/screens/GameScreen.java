package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
	private final ScoreDialog scoreDialog;

	private Level level;
	private LevelRenderer renderer;
	private final InputMultiplexer inputMux = new InputMultiplexer();

	private boolean dialogShowing = false;

	public GameScreen(TiltAndTumble game, int currentLevel) {
		super(game);
		ballController = new BallController(!game.getSettings().isUseDpad());
		worldPopulator = new WorldPopulator();
		scoreDialog = new ScoreDialog("Score\n", skin, this);

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
		renderer = new DefaultLevelRenderer(level, game.getWidth(), game.getHeight());
		if (game.getSettings().isDebugRender()) {
			renderer = new DebugLevelRenderer(renderer, ballController);
		}
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

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
		inputMux.addProcessor(stage);
	}

	@Override
	protected void preStageRenderHook(float delta) {
		renderer.render(game.getSpriteBatch(), game.getFont());
		if (level.isStarted()) {
			level.update();
		} else if (level.hasFinished()) {

			// NOTE: we can only call show once and this hook gets called for
			//       each frame we render. something better can be done instead
			//       of this boolean gate.
			if (!dialogShowing) {
				dialogShowing = true;
				scoreDialog.show(stage);
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

	public static final class ScoreDialog extends Dialog {

		private final GameScreen screen;

		public ScoreDialog(String title, Skin skin, GameScreen screen) {
			super(title, skin);
			this.screen = screen;
			text("Score: TODO");
			button("Continue");
		}

		@Override
		protected void result(Object object) {
			super.result(object);
			Gdx.app.log("dialog result", "" + object);
			screen.loadNextLevel();
		}
	}
}
