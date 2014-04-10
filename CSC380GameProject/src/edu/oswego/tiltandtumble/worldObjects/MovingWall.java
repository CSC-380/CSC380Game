package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.Level;
import edu.oswego.tiltandtumble.levels.UnitScale;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;
import edu.oswego.tiltandtumble.worldObjects.paths.MovementStrategy;

public class MovingWall extends AbstractWorldObject
		implements MapRenderable, WorldUpdateable, Disposable,
		BallCollisionListener, Activatable {
	public static final float FRICTION = 0.5f;
	public static final float DENSITY = 5.0f;
	public static final float RESTITUTION = 0.0f;
	public static final BodyType BODY_TYPE = BodyType.KinematicBody;

	// in meters per second
	public static final float DEFAULT_SPEED = 1f;
	public static final String DEFAULT_SPRITE = "WallGrey2x2.png";

	private final UnitScale scale;
	private final MovementStrategy movement;
	private final GraphicComponent graphic;

	private boolean collidingWithBall = false;
	private Ball ball;
    private final Level level;

	public MovingWall(Body body, MovementStrategy movement,
			GraphicComponent graphic, UnitScale scale, Level level) {
		super(body);

		this.scale = scale;
		this.level = level;
		this.graphic = graphic;

		this.movement = movement;
	}

	@Override
	public void drawBeforeBall(float delta, SpriteBatch batch) {
	}

	@Override
	public void drawAfterBall(float delta, SpriteBatch batch) {
		graphic.setPosition(scale.metersToPixels(body.getPosition().x),
				scale.metersToPixels(body.getPosition().y));
		graphic.draw(delta, batch);
	}

	@Override
	public void update(float delta) {
		move(delta);

		if (collidingWithBall) {
			if (body.getFixtureList().get(0).testPoint(ball.getBody().getPosition())) {
				Gdx.app.log("MovingWall", "Wall SMASH Ball!");
				level.finish(true);
			}
		}
	}

	private void move(float timeDelta) {
		body.setTransform(movement.move(body.getPosition(), timeDelta), 0);
	}

	@Override
	public void dispose() {
		graphic.dispose();
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
	public void activate() {
		movement.activate();
	}

	@Override
	public void deactivate() {
		movement.deactivate();
	}
}
