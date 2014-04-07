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


	private boolean collidingWithBall = false;
	private Ball ball;

	public AttractorForce(Body body, float speed, UnitScale scale) {
		super(body);
		this.scale = scale;
	}

	@Override
	public void update(float delta) {
		if(collidingWithBall) {
			
			float distanceToMiddle = ball.getMapX() - scale.metersToPixels(body.getPosition().x);
			float radius = distanceToMiddle;
			float tempX, tempY;
		
			Vector2 ballPosition = new Vector2();
			ballPosition.x = ball.getMapX();
			ballPosition.y = ball.getMapY();
			Vector2 ballDistance = new Vector2(0,0);
			ballDistance.add(ballPosition);
			ballDistance.sub(body.getPosition());
			float finalDistance = ballDistance.len();
		
			tempY = -ballDistance.y;
			tempX = -ballDistance.x;
			ballDistance.x = tempY;
			ballDistance.y = tempX;

			float vectorSum = Math.abs(ballDistance.x) + Math.abs(ballDistance.y);
			ballDistance.mul((1/vectorSum)*radius/finalDistance);
		
			ball.applyLinearImpulse(ballDistance.x, ballDistance.y);


		}
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