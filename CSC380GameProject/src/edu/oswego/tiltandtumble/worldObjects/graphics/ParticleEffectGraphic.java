package edu.oswego.tiltandtumble.worldObjects.graphics;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticleEffectGraphic implements GraphicComponent {

	private final ParticleEffect effect;

	public ParticleEffectGraphic(ParticleEffect effect) {
		this.effect = effect;
	}

	@Override
	public void start() {
		effect.start();
	}

	@Override
	public void setPosition(float x, float y) {
		effect.setPosition(x, y);
	}

	@Override
	public void setRotation(float degrees) {
		// noop
	}

	@Override
	public void setSize(float width, float height) {
		// noop
	}

	@Override
	public void draw(float delta, SpriteBatch batch) {
		effect.draw(batch, delta);
	}

	@Override
	public void dispose() {
		effect.dispose();
	}

	@Override
	public boolean isFinished() {
		return effect.isComplete();
	}
}
