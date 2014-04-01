package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.levels.UnitScale;

public class Ball extends AbstractWorldObject implements MapRenderable,
		Disposable, Audible {
	public static final float FRICTION = 0.1f;
	public static final float DENSITY = 1.0f;
	public static final float RESTITUTION = 0.5f;
	public static final BodyType BODY_TYPE = BodyType.DynamicBody;
	public static final float ANGULAR_DAMPENING = 0.1f;
	public static final float LINEAR_DAMPENING = 0.1f;

	private final Texture texture;
	private final Sprite sprite;

	private final UnitScale scale;
	private boolean playSound;
	private final Sound sound;

	public Ball(Body body, float diameter, UnitScale scale) {
		super(body);
		this.scale = scale;

		texture = new Texture(Gdx.files.internal("data/GreenOrb.png"));
		sprite = new Sprite(texture);

		sprite.setSize(diameter, diameter);

		playSound = true;
		sound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/boing1.wav"));
	}

	public void applyLinearImpulse(float x, float y) {
		body.applyLinearImpulse(x, y, body.getPosition().x,
				body.getPosition().y, true);
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.setPosition(getMapX(), getMapY());
		sprite.draw(batch);
	}

	public float getMapX() {
		return scale.metersToPixels(body.getPosition().x) - (sprite.getWidth() * 0.5f);
	}

	public float getMapY() {
		return scale.metersToPixels(body.getPosition().y) - (sprite.getHeight() * 0.5f);
	}

	@Override
	public void dispose() {
		texture.dispose();
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
