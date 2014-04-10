package edu.oswego.tiltandtumble.worldObjects;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;

public class TimedSwitch extends AbstractWorldObject implements
		BallCollisionListener, WorldUpdateable, Switch {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;
	public static final float DEFAULT_INTERVAL = 3;

	private State currentState;
	private final Collection<Activatable> activatables;
	private final float interval;
	private float elapsed;

	public TimedSwitch(Body body, float interval) {
		super(body);
		this.interval = interval;
		activatables = new LinkedList<Activatable>();
		currentState = State.ON;
	}

	@Override
	public void addActivatable(Activatable a) {
		activatables.add(a);
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		currentState.on(this);
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
	}

	@Override
	public void update(float delta) {
		elapsed += delta;
		currentState.off(this);
	}

	private void changeState(State state) {
		currentState = state;
	}

	private static enum State {
		ON {
			@Override
			public void off(TimedSwitch s) {
				s.changeState(OFF);
				for (Activatable a : s.activatables) {
					a.deactivate();
				}
			}
		},
		OFF {
			@Override
			public void on(TimedSwitch s) {
				if (s.elapsed >= s.interval) {
					s.changeState(ON);
					for (Activatable a : s.activatables) {
						a.activate();
					}
				}
			}
		};

		public void on(TimedSwitch s) {}
		public void off(TimedSwitch s) {}
	}
}
