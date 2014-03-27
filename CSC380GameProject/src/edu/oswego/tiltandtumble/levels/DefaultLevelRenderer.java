package edu.oswego.tiltandtumble.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class DefaultLevelRenderer implements LevelRenderer {
	private final OrthographicCamera camera;
	private final OrthogonalTiledMapRenderer mapRenderer;

	private final Level level;
	private final float width;
	private final float height;
	private final int mapWidth;
	private final int mapHeight;

	private final Texture texture;
	private final TextureRegion background;

	public DefaultLevelRenderer(Level level, float viewportWidth, float viewportHeight) {
		this.level = level;
		width = viewportWidth;
		height = viewportHeight;

		MapProperties prop = level.getMap().getProperties();

		mapWidth = prop.get("width", Integer.class)
				* prop.get("tilewidth", Integer.class);
		mapHeight = (prop.get("height", Integer.class)
				* prop.get("tileheight", Integer.class))
				+ 32; // adding extra for HUD

		camera = new OrthographicCamera();

		// NOTE: if we set the scaling based on the texture size then
		// we can use tile counts, instead of pixels, for the
		// camera.setToOrtho call below.
		mapRenderer = new OrthogonalTiledMapRenderer(level.getMap(), 1);
		mapRenderer.setView(camera);

		camera.setToOrtho(false, width, height);

		updateCamera();

		// TODO: parallax scrolling would be kind of cool to add...
		String bgFile = prop.get("background image", String.class);
		if (bgFile != null) {
			texture = new Texture(Gdx.files.internal("data/" + bgFile));
			background = new TextureRegion(texture);
		}
		else {
			texture = null;
			background = null;
		}
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
	public void render(float delta, SpriteBatch batch, BitmapFont font) {
		updateCamera();

		if (background != null) {
			batch.begin();
			batch.draw(background, 0, 0);
			batch.end();
		}

		mapRenderer.setView(camera);
		mapRenderer.render();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		level.draw(batch);
		batch.end();
	}

	@Override
	public void updateCamera() {
		// we need to always set the position and update the camera
		// but we need to make sure the position is valid...

		float x = level.getBall().getMapX();
		if (x > (mapWidth - (width / 2))) {
			x = mapWidth - (width / 2);
		} else if (x < (width / 2)) {
			x = width / 2;
		}

		float y = level.getBall().getMapY();
		if (y > (mapHeight - (height / 2))) {
			y = mapHeight - (height / 2);
		} else if (y < (height / 2)) {
			y = height / 2;
		}

		// the camera center.
		camera.position.set(
				x,
				y,
				camera.position.z);
		camera.update();
	}

	@Override
	public void dispose() {
		mapRenderer.dispose();
		if (texture != null) {
			texture.dispose();
		}
	}
}
