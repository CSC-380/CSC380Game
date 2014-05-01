package edu.oswego.tiltandtumble.worldObjects.graphics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteGraphic implements GraphicComponent {
	private final Sprite sprite;

	public SpriteGraphic(Sprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public void start() { }

	@Override
	public void setPosition(float x, float y) {
		sprite.setPosition(x - sprite.getOriginX(), y - sprite.getOriginY());
	}

	@Override
	public void setRotation(float degrees) {
		sprite.setRotation(degrees);
	}

	@Override
	public void draw(float delta, SpriteBatch batch) {
		sprite.draw(batch);
	}

	@Override
	public void setSize(float width, float height) {
		sprite.setSize(width, height);
	}

	@Override
	public void dispose() { }

	@Override
	public boolean isFinished() {
		return true;
	}
}
