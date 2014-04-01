package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.levels.BallController;

public class TeleporterTarget extends AbstractWorldObject
		implements WorldUpdateable, Audible, Disposable {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	private final boolean resetVelocity;
	private final BallController ballController;
	private Ball pendingWarp = null;
	private Vector2 pendingVelocity = null;

	private boolean playSound;
	private final Sound sound;

	public TeleporterTarget(Body body, boolean resetVelocity, BallController ballController) {
		super(body);
		this.resetVelocity = resetVelocity;
		this.ballController = ballController;

		playSound = true;
		sound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/laser4.mp3"));
	}

	public void warp(Ball ball) {
		if (resetVelocity) {
			ball.getBody().setLinearVelocity(0, 0);
			ball.getBody().setAngularVelocity(0);
			ballController.resetBall();
		}
		else {
			pendingVelocity = ball.getBody().getLinearVelocity();
		}
		pendingWarp = ball;
	}

	@Override
	public void update(float delta) {
		if (pendingWarp != null) {
			// calling setTransform from inside the contact listener will
			// crash the game, so we wait and do it in the update
			pendingWarp.getBody().setTransform(body.getPosition(), 0);
			if (pendingVelocity != null) {
				// see if this fixes the velocity issue....
				pendingWarp.getBody().setLinearVelocity(pendingVelocity);
				pendingVelocity = null;
			}
			playSound();
			pendingWarp = null;
		}
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
