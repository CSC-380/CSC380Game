package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.levels.BallController;
import edu.oswego.tiltandtumble.levels.UnitScale;

public class TeleporterTarget extends AbstractWorldObject
		implements WorldUpdateable, Audible, Disposable, MapRenderable {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	private final boolean resetVelocity;
	private final BallController ballController;
	private Ball pendingWarp = null;
	private Vector2 pendingVelocity = null;

	private boolean playSound;
	private final Sound sound;
	private final ParticleEffect effect;

	public TeleporterTarget(Body body, boolean resetVelocity,
			BallController ballController, UnitScale scale) {
		super(body);
		this.resetVelocity = resetVelocity;
		this.ballController = ballController;

		playSound = true;
		sound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/laser4.mp3"));

		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("data/teleporter.p"), Gdx.files.internal("data"));
		effect.setPosition(
				scale.metersToPixels(body.getPosition().x),
				scale.metersToPixels(body.getPosition().y));
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
			effect.start();
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
		effect.dispose();
	}

	@Override
	public void draw(float delta, SpriteBatch batch) {
		effect.draw(batch, delta);
	}
}
