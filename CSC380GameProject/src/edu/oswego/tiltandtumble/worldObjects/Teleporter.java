package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.BallController;
import edu.oswego.tiltandtumble.levels.UnitScale;

public class Teleporter extends TeleporterTarget
		implements BallCollisionListener {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	// in seconds
	public static final float WAIT_TIME = 3;
	private final float waitTime;

	private static enum State {
		DISABLED,
		WAITING,
		ACTIVE
	}

	private final TeleporterSelectorStrategy selector;
	private float disabledTime = 0;
	private State currentState;

	public Teleporter(Body body, TeleporterSelectorStrategy selector,
			boolean resetVelocity, BallController ballController,
			UnitScale scale, float waitTime) {
		super(body, resetVelocity, ballController, scale);
		this.selector = selector;
		this.waitTime = waitTime;
		currentState = State.ACTIVE;
	}

	@Override
	public void warp(Ball ball) {
		super.warp(ball);
		currentState = State.DISABLED;
		disabledTime = 0;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (currentState == State.WAITING) {
			disabledTime += delta;
			if (disabledTime > waitTime) {
				currentState = State.ACTIVE;
			}
		}
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		if (currentState != State.ACTIVE) return;
		TeleporterTarget target = selector.getNext();
		target.warp(ball);
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
		if (currentState == State.DISABLED) {
			currentState = State.WAITING;
		}
	}
}
