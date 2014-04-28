package edu.oswego.tiltandtumble.worldObjects;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;

public class TimedSwitch extends AbstractWorldObject implements
		BallCollisionListener, WorldUpdateable, Switch, Audible, Disposable,
		MapRenderable {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;
	public static final float DEFAULT_INTERVAL = 3;

	private State currentState;
	private final State startState;
	private final Collection<Activatable> activatables;
	private final float interval;
	private float elapsed;

	private boolean playSound;
	private final Sound sound;

	private final GraphicComponent graphicOn;
	private final GraphicComponent graphicOff;

	public TimedSwitch(Body body, float interval, boolean startOn,
			GraphicComponent graphicOn, GraphicComponent graphicOff,
			AssetManager assetManager) {
		super(body);
		this.interval = interval;
		activatables = new LinkedList<Activatable>();
		if (startOn) {
			currentState = State.ON;
		} else {
			currentState = State.OFF;
		}

		this.graphicOn = graphicOn;
		this.graphicOff = graphicOff;

		startState = currentState;

		playSound = true;
		String soundFile = "data/soundfx/switch.ogg";
		if (!assetManager.isLoaded(soundFile)) {
			assetManager.load(soundFile, Sound.class);
			assetManager.finishLoading();
		}
		sound = assetManager.get(soundFile, Sound.class);
	}

	@Override
	public void addActivatable(Activatable a) {
		if (startState == State.ON) {
			a.activate();
		} else {
			a.deactivate();
		}
		activatables.add(a);
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		currentState.start(this);
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
	}

	@Override
	public void drawBeforeBall(float delta, SpriteBatch batch) {
		currentState.draw(this, delta, batch);
	}

	@Override
	public void drawAfterBall(float delta, SpriteBatch batch) {
	}

	@Override
	public void update(float delta) {
		currentState.update(this, delta);
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
	public void endSound() {
		sound.stop();
	}

	@Override
	public void dispose() {
		graphicOff.dispose();
		graphicOn.dispose();
	}

	private void changeState(State state) {
		currentState = state;
	}

	private static enum State {
		TIMER {
			@Override
			public void update(TimedSwitch s, float delta) {
				s.elapsed += delta;
				if (s.elapsed >= s.interval) {
					s.changeState(s.startState);
					s.elapsed = 0;
					s.startState.toggle(s);
				}
			}

			@Override
			public void draw(TimedSwitch s, float delta, SpriteBatch batch) {
				if (s.startState == ON) {
					s.graphicOff.draw(delta, batch);
				} else {
					s.graphicOn.draw(delta, batch);
				}
			}
		},
		ON {
			@Override
			public void start(TimedSwitch s) {
				s.playSound();
				s.changeState(TIMER);
				toggle(s);
			}

			@Override
			public void toggle(TimedSwitch s) {
				for (Activatable a : s.activatables) {
					a.deactivate();
				}
			}

			@Override
			public void draw(TimedSwitch s, float delta, SpriteBatch batch) {
				s.graphicOn.draw(delta, batch);
			}
		},
		OFF {
			@Override
			public void start(TimedSwitch s) {
				s.playSound();
				s.changeState(TIMER);
				toggle(s);
			}

			@Override
			public void toggle(TimedSwitch s) {
				for (Activatable a : s.activatables) {
					a.activate();
				}
			}

			@Override
			public void draw(TimedSwitch s, float delta, SpriteBatch batch) {
				s.graphicOff.draw(delta, batch);
			}
		};

		public void start(TimedSwitch s) {}
		public void update(TimedSwitch s, float delta) {}
		protected void toggle(TimedSwitch s) {}
		public void draw(TimedSwitch s, float delta, SpriteBatch batch) {}
	}
}
