package edu.oswego.tiltandtumble.worldObjects.paths;

import com.badlogic.gdx.math.Vector2;

public interface MovementStrategy {
	public void activate();
	public void deactivate();
	public Vector2 move(Vector2 start, float timeDelta);
}
