package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface MapRenderable {
	public void drawBeforeBall(float delta, SpriteBatch batch);
	public void drawAfterBall(float delta, SpriteBatch batch);
}
