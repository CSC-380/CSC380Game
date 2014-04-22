package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.Level;

public class FinishLine extends AbstractWorldObject
		implements BallCollisionListener, Disposable  {
    public static final BodyType BODY_TYPE = BodyType.StaticBody;
    public static final boolean IS_SENSOR = true;

    private final Level level;

	private final boolean playSound;
	private final Sound sound;

	public FinishLine(Body body, Level level) {
		super(body);
		this.level = level;

		playSound = true;
		sound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/finishLine.ogg"));
	}

	/**
	 * This will end the level as soon as the ball makes contact with this object
	 * so you may want the collision object on the map to be smaller than its
	 * graphical representation so that it looks like the ball enters or crosses
	 * a finish area rather than just touches it.
	 */
	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		Gdx.app.log("FinishLine", "Ball enter");
		sound.play();
		level.win();
		level.exit();
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
	}

	@Override
	public void dispose() {
		sound.dispose();
	}
}
