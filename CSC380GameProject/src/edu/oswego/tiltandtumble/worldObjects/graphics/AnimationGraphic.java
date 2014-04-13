package edu.oswego.tiltandtumble.worldObjects.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AnimationGraphic implements GraphicComponent {
	private final Animation ani;
	private final Vector2 position;
	private final Vector2 origin;
	private final Vector2 dimension;
	private final Vector2 scale;
	private final boolean looping;

	private float aniTime = 0;
	private float angle = 0;


	public static class Builder {
		private final TextureRegion[] frames;
		private final Vector2 origin = new Vector2(0, 0);
		private final Vector2 position = new Vector2(0, 0);
		private final Vector2 dimension = new Vector2();
		private final Vector2 scale = new Vector2(1, 1);

		private final float duration;

		private boolean isLooping = false;
		private int looping = 0;

		public Builder(TextureRegion sheet, int rows, int columns, float duration) {
			int width = sheet.getRegionWidth() / columns;
			int height = sheet.getRegionHeight() / rows;

			TextureRegion[][] tmp = sheet.split(width, height);
			frames = new TextureRegion[rows * columns];
			int index = 0;
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					frames[index++] = new TextureRegion(tmp[i][j]);
				}
			}
			this.duration = duration;
			dimension.set(width, height);
		}

		public Builder width(float width) {
			dimension.x = width;
			return this;
		}

		public Builder origin(float x, float y) {
			origin.set(x, y);
			return this;
		}

		public Builder position(float x, float y) {
			position.set(x, y);
			return this;
		}

		public Builder scale(float x, float y) {
			scale.set(x, y);
			return this;
		}

		public Builder looping(int looping) {
			this.isLooping = true;
			this.looping = looping;
			return this;
		}

		public Builder center() {
			origin.set(dimension.x / 2, dimension.y / 2);
			return this;
		}

		public Builder centerX() {
			origin.set(dimension.x / 2, 0);
			return this;
		}

		public Builder centerY() {
			origin.set(0, dimension.y / 2);
			return this;
		}

		public AnimationGraphic build() {
			return new AnimationGraphic(this);
		}
	}

	private AnimationGraphic(Builder builder) {
		position = builder.position;
		origin = builder.origin;
		dimension = builder.dimension;
		scale = builder.scale;

		looping = builder.isLooping;
		if (builder.isLooping) {
			ani = new Animation(
					builder.duration / builder.frames.length,
					new Array<TextureRegion>(builder.frames),
					builder.looping);
		} else {
			ani = new Animation(
					builder.duration / builder.frames.length,
					new Array<TextureRegion>(builder.frames));
		}
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
	public void setRotation(float degrees) {
		angle = degrees;
	}

	@Override
	public void setSize(float width, float height) {
		dimension.set(width, height);
	}

	@Override
	public void draw(float delta, SpriteBatch batch) {
		aniTime += delta;
		TextureRegion region = ani.getKeyFrame(aniTime, looping);
		batch.draw(region,
				position.x - origin.x, position.y - origin.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				scale.x, scale.y,
				angle);
	}

	@Override
	public void dispose() { }
}
