package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.UnitScale;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;

public class AttractorForce extends AbstractWorldObject
		implements WorldUpdateable, BallCollisionListener, MapRenderable,
		Disposable, Audible {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	public static final float DEFAULT_SPEED = 1f;

	private final float speed;
	private final float radius;

	private final GraphicComponent graphic;
	private final UnitScale scale;

	private boolean collidingWithBall = false;
	private Ball ball;

	private boolean playSound;
	private final Sound sound;

	public AttractorForce(Body body, float speed, float radius,
			GraphicComponent graphic, UnitScale scale) {
		super(body);
		this.speed = speed;
		this.radius = radius;
		this.graphic = graphic;
		this.scale = scale;

		playSound = true;
		sound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/attractor.ogg"));
	}

	@Override
	public void update(float delta) {
		if (collidingWithBall) {
			float angle = angle(body, ball);

			Vector2 currentVelocity = ball.body.getLinearVelocity();

			// we want some affect from the delta to help normalize the force
			// across frame rates, scale with distance as well.
			float distance = body.getPosition().dst(ball.getBody().getPosition());
			if (distance == 0) return;
			float intensity = (speed * delta) * (radius / distance);

			Vector2 direction = new Vector2();
			direction.x = currentVelocity.x
					+ (intensity * (float)Math.cos(angle));
			direction.y = currentVelocity.y
					+ (intensity * (float)Math.sin(angle));

			ball.getBody().setLinearVelocity(direction);

			// TODO: make the graphic scale in size to the distance for the ball
			Vector2 line = new Vector2(ball.getBody().getPosition());
			line.sub(body.getPosition());
			graphic.setRotation(line.angle() - 90);
			float len = ball.body.getPosition().dst(body.getPosition());
			graphic.setSize(ball.getRadius() * 2, scale.metersToPixels(len));
		}
	}

	private float angle(Body a, Ball b)
	{
		float dx = a.getPosition().x - b.getBody().getPosition().x;
		float dy = a.getPosition().y - b.getBody().getPosition().y;
		return (float) Math.atan2(dy, dx);
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		collidingWithBall = true;
		this.ball = ball;
		graphic.start();
		if (playSound) {
			sound.loop();
		}
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
		collidingWithBall = false;
		this.ball = null;
		sound.stop();
	}

	@Override
	public void drawBeforeBall(float delta, SpriteBatch batch) {
	}

	@Override
	public void drawAfterBall(float delta, SpriteBatch batch) {
		if (collidingWithBall) {
			graphic.draw(delta, batch);
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
		graphic.dispose();
	}
}
