package edu.oswego.tiltandtumble.worldObjects;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;

public class ToggleSwitch extends AbstractWorldObject
		implements BallCollisionListener, Switch, Audible, Disposable {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	protected State currentState;
	private final Collection<Activatable> activatables;

	private boolean playSound;
	private final Sound offsound;
	private final Sound onsound;

	public ToggleSwitch(Body body) {
		super(body);
		activatables = new LinkedList<Activatable>();
		currentState = State.OFF;

		playSound = true;
		offsound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/switch-off.ogg"));
		onsound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/switch-on.ogg"));
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

	@Override
	public void setPlaySound(boolean value) {
		playSound = value;
	}

	@Override
	public void playSound() {
		if (playSound) {
			currentState.playSound(this);
		}
	}

	@Override
	public void dispose() {
		offsound.dispose();
		onsound.dispose();
	}

	private void changeState(State state) {
		currentState = state;
	}

	protected static enum State {
		ON {
			@Override
			public void toggle(ToggleSwitch s) {
				playSound(s);
				s.changeState(OFF);
				for (Activatable a : s.activatables) {
					a.deactivate();
				}
			}

			@Override
			public void playSound(ToggleSwitch s) {
				s.offsound.play();
			}
		},
		OFF {
			@Override
			public void toggle(ToggleSwitch s) {
				playSound(s);
				s.changeState(ON);
				for (Activatable a : s.activatables) {
					a.activate();
				}
			}

			@Override
			public void playSound(ToggleSwitch s) {
				s.onsound.play();
			}
		};

		public void toggle(ToggleSwitch s) {}
		public void playSound(ToggleSwitch s) {}
	}
}
