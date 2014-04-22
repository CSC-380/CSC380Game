package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.Level;

public class StaticWall extends AbstractWorldObject implements WorldUpdateable,
		BallCollisionListener {
    public static final float FRICTION = 0.5f;
    public static final float DENSITY = 5.0f;
    public static final float RESTITUTION = 0.0f;
    public static final BodyType BODY_TYPE = BodyType.StaticBody;

	private boolean collidingWithBall = false;
	private Ball ball;
    private final Level level;

	public StaticWall(Body body, Level level) {
        super(body);
		this.level = level;
    }

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		collidingWithBall = true;
		this.ball = ball;
		ball.playSound();
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
		collidingWithBall = false;
		this.ball = null;
	}

	@Override
	public void update(float delta) {
		if (collidingWithBall) {
			if (body.getFixtureList().get(0).testPoint(ball.getBody().getPosition())) {
				Gdx.app.log("StaticWall", "Wall SMASH Ball!");
				level.fail();
				level.exit();
			}
		}
	}
}
