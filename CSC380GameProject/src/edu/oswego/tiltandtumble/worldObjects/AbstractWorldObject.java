package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.physics.box2d.Body;

abstract class AbstractWorldObject implements WorldObject {
	protected final Body body;

	AbstractWorldObject(Body body) {
		this.body = body;
	}

	@Override
	public Body getBody() {
		return body;
	}
}
