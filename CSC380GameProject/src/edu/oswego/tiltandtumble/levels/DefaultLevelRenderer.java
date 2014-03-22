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
	float width;
	float height;
	int mapX;
	int mapY;

	public DefaultLevelRenderer(Level level, float viewportWidth, float viewportHeight) {
		this.level = level;
		width = viewportWidth;
		height = viewportHeight;
		mapX = level.getMapWidth();
		mapY = level.getMapHeight() + 30;//adding 30 for HUD
		camera = new OrthographicCamera();

		// NOTE: if we set the scaling based on the texture size then
		// we can use tile counts, instead of pixels, for the
		// camera.setToOrtho call below.
		mapRenderer = new OrthogonalTiledMapRenderer(level.getMap(), 1);
		mapRenderer.setView(camera);


		// TODO: figure out how to scale this to different screen sizes
		camera.setToOrtho(false, width, height);
		if(!this.isBallInSafeXLeft()){
			camera.position.set(
					width/2,
					height/2,
					camera.position.z);
		camera.update();
		}else if(!this.isBallInSafeXRight()){
			camera.position.set(
					mapX - width/2,
					height/2,
					camera.position.z);
		camera.update();
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
		if(this.isBallInSafeXRight() && this.isBallInSafeXLeft()){
			camera.position.set(
					level.getBall().getMapX(),
					camera.position.y,
					camera.position.z);
		}
		if(this.isBallInSafeYTop() && this.isBallInSafeYBottom()){
			camera.position.set(
					camera.position.x,
					level.getBall().getMapY(),
					camera.position.z);
		}
		camera.update();
	}

	private boolean isBallInSafeXRight(){
		float ballX = level.getBall().getMapX();
		return ballX + (width/2) < mapX;
	}

	private boolean isBallInSafeXLeft(){
		float ballX = level.getBall().getMapX();
		return ballX - (width/2) > 0;
	}

	private boolean isBallInSafeYTop(){
		float ballY = level.getBall().getMapY();
		return ballY + (height/2) < mapY;
	}

	private boolean isBallInSafeYBottom(){
		float ballY = level.getBall().getMapY();;
		return ballY - (height/2) > 0;
	}

	@Override
	public void dispose() {
		mapRenderer.dispose();
	}
}
