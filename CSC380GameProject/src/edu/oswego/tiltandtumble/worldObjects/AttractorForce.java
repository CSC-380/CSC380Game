package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.UnitScale;

public class AttractorForce extends AbstractWorldObject implements WorldUpdateable, BallCollisionListener {

	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	//in meters per second
	public static final float DEFAULT_SPEED = 1f;

	private final UnitScale scale;
	private final float speed;

	private boolean collidingWithBall = false;
	private Ball ball;

	public AttractorForce(Body body, float speed, UnitScale scale) {
		super(body);
		this.scale = scale;
		this.speed = speed;
	}

	@Override
	public void update(float delta) {
		if (collidingWithBall) {
			float angle = angle(body, ball);

			Vector2 currentVelocity = ball.body.getLinearVelocity();

			// we want some affect from the delta to help normalize the force
			// across frame rates, scale with distance as well.
            float intensity = (speed * delta)
            		/ body.getPosition().dst(ball.getBody().getPosition());
			Vector2 direction = new Vector2();
			direction.x = currentVelocity.x + (intensity * (float)Math.cos(angle));
			direction.y = currentVelocity.y + (intensity * (float)Math.sin(angle));

			ball.body.setLinearVelocity(direction);
		}
	}

	private float angle(Body a, Ball b)
	{
		float dx = a.getPosition().x - b.getBody().getPosition().x;
		float dy = a.getPosition().y - b.getBody().getPosition().y;
		return (float) Math.atan2(dy, dx);
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		collidingWithBall = true;
		this.ball = ball;
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
		collidingWithBall = false;
		this.ball = null;

	}
}