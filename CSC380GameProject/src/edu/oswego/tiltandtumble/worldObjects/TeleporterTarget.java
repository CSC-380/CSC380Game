package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import edu.oswego.tiltandtumble.levels.BallController;

public class TeleporterTarget extends AbstractWorldObject implements WorldUpdateable {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	private final boolean resetVelocity;
	private final BallController ballController;
	private Ball pendingWarp = null;

	public TeleporterTarget(Body body, boolean resetVelocity, BallController ballController) {
		super(body);
		this.resetVelocity = resetVelocity;
		this.ballController = ballController;
	}

	public void warp(Ball ball) {
		if (resetVelocity) {
			ball.getBody().setLinearVelocity(0, 0);
			ball.getBody().setAngularVelocity(0);
			ballController.resetBall();
		}
		pendingWarp = ball;
	}

	@Override
	public void update(float delta) {
		if (pendingWarp != null) {
			// calling setTransform from inside the contact listener will
			// crash the game, so we wait and do it in the update
			pendingWarp.getBody().setTransform(body.getPosition(), 0);
			pendingWarp = null;
		}
	}
}
