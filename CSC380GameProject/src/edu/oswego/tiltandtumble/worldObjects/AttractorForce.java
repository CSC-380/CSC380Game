package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.physics.box2d.Body;

public class AttractorForce extends AbstractWorldObject {

	// TODO: we will will need to simulate radial gravity for this

	public AttractorForce(Body body) {
		super(body);
	}
}
