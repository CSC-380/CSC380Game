package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.Level;

public class FinishLine extends AbstractWorldObject implements BallCollisionListener  {
    public static final BodyType BODY_TYPE = BodyType.StaticBody;
    public static final boolean IS_SENSOR = true;

    private final Level level;

	public FinishLine(Body body, Level level) {
		super(body);
		this.level = level;
	}

	/**
	 * This will end the level as soon as the ball makes contact with this object
	 * so you may want the collision object on the map to be smaller than its
	 * graphical representation so that it looks like the ball enters or crosses
	 * a finish area rather than just touches it.
	 */
	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
        Gdx.app.log("FinishLine", "Ball enter");
        level.win();
        level.exit();
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
	}
}
