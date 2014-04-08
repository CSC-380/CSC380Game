package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.Level;

public class Hole extends AbstractWorldObject implements BallCollisionListener,
		Audible, Disposable {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	private final Level level;

	private boolean playSound;
	private final Sound sound;

	public Hole(Body body, Level level) {
		super(body);
		this.level = level;

		playSound = true;
		sound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/failure-2.ogg"));
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		Gdx.app.log("Hole", "FALL!!");
		playSound();
		level.finish(true);
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
			sound.play();
		}
	}

	@Override
	public void dispose() {
		sound.dispose();
	}
}
