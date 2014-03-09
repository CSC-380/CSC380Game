package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class StaticWall extends AbstractWorldObject {
    public static final float FRICTION = 0.5f;
    public static final float DENSITY = 5.0f;
    public static final float RESTITUTION = 0.0f;
    public static final BodyType BODY_TYPE = BodyType.StaticBody;

    public StaticWall(Body body) {
        super(body);
        body.setUserData(this);
    }
}
