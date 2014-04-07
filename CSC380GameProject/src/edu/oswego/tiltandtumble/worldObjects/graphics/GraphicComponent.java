package edu.oswego.tiltandtumble.worldObjects.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public interface GraphicComponent extends Disposable {
	public void start();
	public void setPosition(float x, float y);
	public void draw(float delta, SpriteBatch batch);
}