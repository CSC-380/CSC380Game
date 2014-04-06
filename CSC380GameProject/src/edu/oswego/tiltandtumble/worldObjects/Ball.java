package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.levels.UnitScale;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;
import edu.oswego.tiltandtumble.worldObjects.graphics.SpriteGraphic;

public class Ball extends AbstractWorldObject implements Disposable, Audible {
	public static final float FRICTION = 0.1f;
	public static final float DENSITY = 1.0f;
	public static final float RESTITUTION = 0.5f;
	public static final BodyType BODY_TYPE = BodyType.DynamicBody;
	public static final float ANGULAR_DAMPENING = 0.1f;
	public static final float LINEAR_DAMPENING = 0.1f;

	private final GraphicComponent graphic;

	private final UnitScale scale;
	private boolean playSound;
	private final Sound sound;

	public Ball(Body body, float diameter, UnitScale scale) {
		super(body);
		this.scale = scale;

		graphic = new SpriteGraphic("data/WorldObjects/GreenOrb.png", diameter, diameter);

		playSound = true;
		sound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/boing1.wav"));
	}

	public void applyLinearImpulse(float x, float y) {
		body.applyLinearImpulse(x, y, body.getPosition().x,
				body.getPosition().y, true);
	}

	public void draw(float delta, SpriteBatch batch) {
		graphic.setPosition(getMapX(), getMapY());
		graphic.draw(delta, batch);
	}

	public float getMapX() {
		return scale.metersToPixels(body.getPosition().x);
	}

	public float getMapY() {
		return scale.metersToPixels(body.getPosition().y);
	}

	@Override
	public void dispose() {
		graphic.dispose();
		sound.dispose();
	}

	@Override
	public void setPlaySound(boolean value) {
		playSound = value;
	}

	@Override
	public void playSound() {
		if (playSound) {
			sound.play(0.2f);
		}
	}
}
