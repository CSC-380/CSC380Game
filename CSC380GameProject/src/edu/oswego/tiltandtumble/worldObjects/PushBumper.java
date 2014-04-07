package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;

public class PushBumper extends AbstractWorldObject implements BallCollisionListener {
    public static final float FRICTION = 0.0f;
    public static final float DENSITY = 2.0f;
    public static final float RESTITUTION = 0.0f;
    public static final BodyType BODY_TYPE = BodyType.StaticBody;

    public static final float DEFAULT_SPEED = 8;
	private final float speed;


    public PushBumper(Body body, float speed) {
        super(body);
        this.speed = speed;
    }

    @Override
	public void handleBeginCollision(Contact contact, Ball ball) {

    	// What this does is instantly accelerate the ball to the defined
    	// "speed" value, if the ball is already moving faster, it will not
    	// accelerate.
		Body target = ball.getBody();
		final float x = Math.abs(target.getLinearVelocity().x);
		final float y = Math.abs(target.getLinearVelocity().y);

		if (x + y > speed) {
			Gdx.app.log("PushBumper", "Ball is moving too fast to accelerate");
			return;
		}

		Gdx.app.log("PushBumper", "Accelerating ball");

		final float forceX = (speed * x) / (x + y);
		final float forceY = (speed * y) / (x + y);

		target.setLinearVelocity(
				forceX * Math.signum(target.getLinearVelocity().x),
				forceY * Math.signum(target.getLinearVelocity().y));
		if (Gdx.input.isPeripheralAvailable(Peripheral.Vibrator)) {
			Gdx.input.vibrate(100);
		}
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
	}
}
