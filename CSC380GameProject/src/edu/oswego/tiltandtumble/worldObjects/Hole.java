package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.Level;

public class Hole extends AbstractWorldObject implements BallCollisionListener {
    public static final BodyType BODY_TYPE = BodyType.StaticBody;
    public static final boolean IS_SENSOR = true;

    private final Level level;

	public Hole(Body body, Level level) {
		super(body);
		this.level = level;
        body.setUserData(this);
	}
	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
        Body target = ball.getBody();
        Gdx.app.log("HOLE", "FALL!!!");
       System.out.println("oh shit hole!");
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
	}
}
