package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.BallController;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;

public class Teleporter extends TeleporterTarget
		implements BallCollisionListener, Activatable {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	// in seconds
	public static final float WAIT_TIME = 3;
	private final float waitTime;

	private final TeleporterSelectorStrategy selector;
	private float disabledTime = 0;
	private State currentState;

	private final GraphicComponent graphic;

	public Teleporter(Body body, TeleporterSelectorStrategy selector,
			boolean resetVelocity, BallController ballController,
			GraphicComponent effect, float waitTime, GraphicComponent teleporter,
			AssetManager assetManager) {
		super(body, resetVelocity, ballController, effect, assetManager);
		this.selector = selector;
		this.waitTime = waitTime;
		this.graphic = teleporter;
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
	public void drawBeforeBall(float delta, SpriteBatch batch) {
		super.drawBeforeBall(delta, batch);
		currentState.animate(this, delta, batch);
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		currentState.handleBeginCollision(this, ball);
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
		currentState.handleEndCollision(this, ball);
	}

	@Override
	public void dispose() {
		super.dispose();
		graphic.dispose();
	}

	@Override
	public void activate() {
		currentState.activate(this);
	}

	@Override
	public void deactivate() {
		currentState.deactivate(this);
	}

	private void changeState(State state) {
		currentState = state;
	}

	private static enum State {
		DEACTIVATED {
			@Override
			public void activate(Teleporter t) {
				t.changeState(ACTIVE);
			}
		},
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
				t.graphic.start();
				t.changeState(DISABLED);
			}
			@Override
			public void handleBeginCollision(Teleporter t, Ball b) {
				TeleporterTarget target = t.selector.getNext();
				target.warp(b);
			}
			@Override
			public void animate(Teleporter t, float delta, SpriteBatch batch) {
				t.graphic.draw(delta, batch);
			}
		};

		public void activate(Teleporter t) {}
		public void deactivate(Teleporter t) {
			t.changeState(DEACTIVATED);
		}
		public void warp(Teleporter t) {}
		public void handleBeginCollision(Teleporter t, Ball b) {}
		public void handleEndCollision(Teleporter t, Ball b) {}
		public void update(Teleporter t, float delta) {}
		public void animate(Teleporter t, float delta, SpriteBatch batch) {}
	}
}
