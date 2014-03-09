package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.physics.box2d.Body;

public class Hole extends AbstractWorldObject {

	public Hole(Body body) {
		super(body);
        body.setUserData(this);
	}
}
