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
		sprite.setPosition(x - (sprite.getWidth() * 0.5f),
				y - (sprite.getHeight() * 0.5f));
	}

	@Override
	public void draw(float delta, SpriteBatch batch) {
		sprite.draw(batch);
	}

	@Override
	public void dispose() { }
}
