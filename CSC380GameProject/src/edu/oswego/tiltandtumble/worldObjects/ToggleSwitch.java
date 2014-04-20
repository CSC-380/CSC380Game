package edu.oswego.tiltandtumble.worldObjects;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;

public class ToggleSwitch extends AbstractWorldObject
		implements BallCollisionListener, Switch, Audible, Disposable,
		MapRenderable {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	protected State currentState;
	private final Collection<Activatable> activatables;

	private boolean playSound;
	private final Sound offsound;
	private final Sound onsound;

	private final GraphicComponent graphicOn;
	private final GraphicComponent graphicOff;

	public ToggleSwitch(Body body, boolean startOn, GraphicComponent graphicOn,
			GraphicComponent graphicOff) {
		super(body);
		activatables = new LinkedList<Activatable>();
		if (startOn) {
			currentState = State.ON;
		} else {
			currentState = State.OFF;
		}

		this.graphicOn = graphicOn;
		this.graphicOff = graphicOff;

		playSound = true;
		offsound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/switch-off.ogg"));
		onsound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/switch-on.ogg"));
	}

	@Override
	public void addActivatable(Activatable a) {
		if (currentState == State.ON) {
			a.activate();
		} else {
			a.deactivate();
		}
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
	public void endSound() {
		offsound.stop();
		onsound.stop();
	}

	@Override
	public void drawBeforeBall(float delta, SpriteBatch batch) {
		currentState.draw(this, delta, batch);
	}

	@Override
	public void drawAfterBall(float delta, SpriteBatch batch) {
	}

	@Override
	public void dispose() {
		offsound.dispose();
		onsound.dispose();
		graphicOff.dispose();
		graphicOn.dispose();
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

			@Override
			public void draw(ToggleSwitch s, float delta, SpriteBatch batch) {
				s.graphicOn.draw(delta, batch);
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

			@Override
			public void draw(ToggleSwitch s, float delta, SpriteBatch batch) {
				s.graphicOff.draw(delta, batch);
			}
		};

		public void toggle(ToggleSwitch s) {}
		public void playSound(ToggleSwitch s) {}
		public void draw(ToggleSwitch s, float delta, SpriteBatch batch) {}
	}
}
