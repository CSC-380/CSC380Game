package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;

public class AttractorForce extends AbstractWorldObject
		implements WorldUpdateable, BallCollisionListener {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	public static final float DEFAULT_SPEED = 1f;

	private final float speed;
	private final float radius;

	private boolean collidingWithBall = false;
	private Ball ball;

	public AttractorForce(Body body, float speed, float radius) {
		super(body);
		this.speed = speed;
		this.radius = radius;
	}

	@Override
	public void update(float delta) {
		if (collidingWithBall) {
			float angle = angle(body, ball);

			Vector2 currentVelocity = ball.body.getLinearVelocity();

			// we want some affect from the delta to help normalize the force
			// across frame rates, scale with distance as well.
			float distance = body.getPosition().dst(ball.getBody().getPosition());
			if (distance == 0) return;
			float intensity = (speed * delta) * (radius / distance);

			Vector2 direction = new Vector2();
			direction.x = currentVelocity.x
					+ (intensity * (float)Math.cos(angle));
			direction.y = currentVelocity.y
					+ (intensity * (float)Math.sin(angle));

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
