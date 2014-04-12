package edu.oswego.tiltandtumble.worldObjects.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ParticleEffectGraphic implements GraphicComponent {

	private final ParticleEffect effect;

	public ParticleEffectGraphic(String name, TextureAtlas atlas) {
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal(name), atlas);
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
	public void draw(float delta, SpriteBatch batch) {
		effect.draw(batch, delta);
	}

	@Override
	public void dispose() {
		effect.dispose();
	}
}
