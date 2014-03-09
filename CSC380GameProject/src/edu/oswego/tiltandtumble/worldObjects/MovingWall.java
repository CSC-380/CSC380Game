package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.physics.box2d.Body;

public class MovingWall extends AbstractWorldObject {
    // TODO: this needs to be a kinematic body

    public MovingWall(Body body) {
    	super(body);
        body.setUserData(this);
    }
}
