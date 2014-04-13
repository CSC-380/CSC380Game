package edu.oswego.tiltandtumble.worldObjects.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * an empty class so we do not need to check for null graphic components if
 * there is none
 */
public class NullGraphic implements GraphicComponent {

	@Override
	public void dispose() { }

	@Override
	public void start() { }

	@Override
	public void setPosition(float x, float y) { }

	@Override
	public void draw(float delta, SpriteBatch batch) { }

	@Override
	public void setRotation(float degrees) { }

	@Override
	public void setSize(float width, float height) { }
}
