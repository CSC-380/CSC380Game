package edu.oswego.tiltandtumble.worldObjects.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteGraphic implements GraphicComponent {
	private final Texture texture;
	private final Sprite sprite;

	public SpriteGraphic(String name, float width, float height) {
		texture = new Texture(Gdx.files.internal(name));
		sprite = new Sprite(texture);
		sprite.setSize(width, height);
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
	public void dispose() {
		texture.dispose();
	}
}
