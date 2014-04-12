package edu.oswego.tiltandtumble.worldObjects.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AnimationGraphic implements GraphicComponent {
	private final Animation ani;
	private final Vector2 position;
	private final int width;
	private final int height;
	private float aniTime;

	public AnimationGraphic(Sprite texture, int rows, int columns, float duration) {
		position = new Vector2();

		width = (int)texture.getWidth() / columns;
		height = (int)texture.getHeight() / rows;

		TextureRegion[][] tmp = texture.split(width, height);
		TextureRegion[] frames = new TextureRegion[rows * columns];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        // TODO: the looping settings should be configurable...
		ani = new Animation(
				duration / frames.length,
				new Array<TextureRegion>(frames),
				Animation.LOOP_PINGPONG);
	}

	@Override
	public void start() {
		aniTime = 0;
	}

	@Override
	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	@Override
	public void draw(float delta, SpriteBatch batch) {
		aniTime += delta;
        batch.draw(ani.getKeyFrame(aniTime, true),
        		position.x - (width * 0.5f),
        		position.y - (height * 0.5f));
	}

	@Override
	public void dispose() { }
}
