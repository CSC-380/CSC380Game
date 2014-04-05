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
		currentState.warp(this);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		currentState.update(this, delta);
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		currentState.handleBeginCollision(this, ball);
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
		currentState.handleEndCollision(this, ball);
	}

	private void changeState(State state) {
		currentState = state;
	}

	private static enum State {
		DISABLED {
			@Override
			public void handleEndCollision(Teleporter t, Ball b) {
				t.changeState(WAITING);
			}
		},
		WAITING {
			@Override
			public void update(Teleporter t, float delta) {
				t.disabledTime += delta;
				if (t.disabledTime > t.waitTime) {
					t.changeState(ACTIVE);
				}
			}
		},
		ACTIVE {
			@Override
			public void warp(Teleporter t) {
				t.disabledTime = 0;
				t.changeState(DISABLED);
			}
			@Override
			public void handleBeginCollision(Teleporter t, Ball b) {
				TeleporterTarget target = t.selector.getNext();
				target.warp(b);
			}
		};

		public void warp(Teleporter t) {}
		public void handleBeginCollision(Teleporter t, Ball b) {}
		public void handleEndCollision(Teleporter t, Ball b) {}
		public void update(Teleporter t, float delta) {}
	}
}
