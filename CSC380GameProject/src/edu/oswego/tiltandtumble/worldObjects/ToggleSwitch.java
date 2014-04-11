package edu.oswego.tiltandtumble.worldObjects;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;

public class ToggleSwitch extends AbstractWorldObject
		implements BallCollisionListener, Switch {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	private State currentState;
	private final Collection<Activatable> activatables;

	public ToggleSwitch(Body body) {
		super(body);
		activatables = new LinkedList<Activatable>();
		currentState = State.OFF;
	}

	@Override
	public void addActivatable(Activatable a) {
		activatables.add(a);
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		currentState.toggle(this);
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
	}

	private void changeState(State state) {
		currentState = state;
	}

	private static enum State {
		ON {
			@Override
			public void toggle(ToggleSwitch s) {
				s.changeState(OFF);
				for (Activatable a : s.activatables) {
					a.deactivate();
				}
			}
		},
		OFF {
			@Override
			public void toggle(ToggleSwitch s) {
				s.changeState(ON);
				for (Activatable a : s.activatables) {
					a.activate();
				}
			}
		};

		public void toggle(ToggleSwitch s) {}
	}
}
