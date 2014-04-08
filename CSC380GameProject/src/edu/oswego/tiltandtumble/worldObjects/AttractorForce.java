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
	public static final float DEFAULT_SPEED = 5f;

	private final UnitScale scale;
	private final float speed;

	private boolean collidingWithBall = false;
	private Ball ball;

	public AttractorForce(Body body, float speed, UnitScale scale) {
		super(body);
		this.scale = scale;
		this.speed = -speed;
	}

	@Override
	public void update(float delta) {
		if(collidingWithBall) {
			
			Vector2 direction = new Vector2();
			double angle = angle(body, ball);
							 
			int intensity = 1;
			Vector2 currentVelocity = ball.body.getLinearVelocity();
			
			direction.x = (float) (currentVelocity.x + intensity * Math.cos(angle)/10);
			direction.y = (float) (currentVelocity.y + intensity * Math.sin(angle)/10);
	
			ball.body.setLinearVelocity(direction);
		}
	}
	private float angle(Body a, Ball b)
	{
		float dx = scale.metersToPixels(a.getPosition().x) - b.getMapX();
		float dy = scale.metersToPixels(a.getPosition().y) - b.getMapY();
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