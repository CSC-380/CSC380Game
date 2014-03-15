package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.oswego.tiltandtumble.TiltAndTumble;

abstract class AbstractScreen implements Screen {

	protected final TiltAndTumble game;
	protected final Stage stage;
	protected final Skin skin;

	public AbstractScreen(final TiltAndTumble game) {
		this.game = game;
		this.stage = game.getStage();
		this.skin = game.getSkin();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		preStageRenderHook(delta);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		postStageRenderHook(delta);
	}

	protected void preStageRenderHook(float delta) {

	}

	protected void postStageRenderHook(float delta) {

	}

	@Override
	public void resize(int width, int height) {
		// NOTE: see https://github.com/libgdx/libgdx/wiki/Scene2d
		//       we may need to do something more complex with viewport scaling
		stage.setViewport(game.getWidth(), game.getHeight(), true);
		stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
	}

	@Override
	public void hide() {
		stage.clear();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}
}
