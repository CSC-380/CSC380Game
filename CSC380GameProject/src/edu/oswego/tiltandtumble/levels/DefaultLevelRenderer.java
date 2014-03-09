package edu.oswego.tiltandtumble.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class DefaultLevelRenderer implements LevelRenderer {
	private final OrthographicCamera camera;
	private final OrthogonalTiledMapRenderer mapRenderer;

	private final Level level;

	public DefaultLevelRenderer(Level level, float viewportWidth, float viewportHeight) {
		this.level = level;

		camera = new OrthographicCamera();

		// NOTE: if we set the scaling based on the texture size then
		// we can use tile counts, instead of pixels, for the
		// camera.setToOrtho call below.
		mapRenderer = new OrthogonalTiledMapRenderer(level.getMap(), 1);
		mapRenderer.setView(camera);

		// TODO: figure out how to scale this to different screen sizes
		camera.setToOrtho(false, viewportWidth, viewportHeight);
	}

	@Override
	public OrthographicCamera getCamera() {
		return camera;
	}

	@Override
	public Level getLevel() {
		return level;
	}

	@Override
	public void render(SpriteBatch batch, BitmapFont font) {
		updateCamera();

		// clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		mapRenderer.setView(camera);
		mapRenderer.render();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		level.draw(batch);
		batch.end();
	}

	@Override
	public void updateCamera() {
		camera.position.set(
				level.getScale().metersToPixels(
						level.getBall().getBody().getPosition().x),
				level.getScale().metersToPixels(
						level.getBall().getBody().getPosition().y),
				camera.position.z);
		camera.update();
	}

	@Override
	public void dispose() {
		mapRenderer.dispose();
	}
}
