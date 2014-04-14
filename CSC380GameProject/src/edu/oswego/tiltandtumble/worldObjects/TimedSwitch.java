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

public class TimedSwitch extends AbstractWorldObject implements
		BallCollisionListener, WorldUpdateable, Switch, Audible, Disposable {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;
	public static final float DEFAULT_INTERVAL = 3;

	private State currentState;
	private final Collection<Activatable> activatables;
	private final float interval;
	private float elapsed;

	private boolean playSound;
	private final Sound sound;

	public TimedSwitch(Body body, float interval) {
		super(body);
		this.interval = interval;
		activatables = new LinkedList<Activatable>();
		currentState = State.OFF;

		playSound = true;
		sound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/switch.ogg"));
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

	@Override
	public void setPlaySound(boolean value) {
		playSound = value;
	}

	@Override
	public void playSound() {
		if (playSound) {
			sound.play();
		}
	}

	@Override
	public void dispose() {
		sound.dispose();
	}

	private void changeState(State state) {
		currentState = state;
	}

	private static enum State {
		ON {
			@Override
			public void off(TimedSwitch s) {
				if (s.elapsed >= s.interval) {
					s.changeState(OFF);
					for (Activatable a : s.activatables) {
						a.deactivate();
					}
				}
			}
		},
		OFF {
			@Override
			public void on(TimedSwitch s) {
				s.elapsed = 0;
				s.playSound();
				s.changeState(ON);
				for (Activatable a : s.activatables) {
					a.activate();
				}
			}
		};

		public void on(TimedSwitch s) {}
		public void off(TimedSwitch s) {}
	}
}
