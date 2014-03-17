package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import edu.oswego.tiltandtumble.levels.Level;

public class Hole extends AbstractWorldObject {
    public static final BodyType BODY_TYPE = BodyType.StaticBody;
    public static final boolean IS_SENSOR = true;

    private final Level level;

	public Hole(Body body, Level level) {
		super(body);
		this.level = level;
        body.setUserData(this);
	}
}
