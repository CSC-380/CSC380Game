package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.levels.UnitScale;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;
import edu.oswego.tiltandtumble.worldObjects.graphics.SpriteGraphic;

public class ShadowBall implements Disposable {

	private final GraphicComponent graphic;
	private final TextureAtlas atlas;
	private Sprite sprite;
	private SpriteBatch batch;
	
	public ShadowBall(SpriteBatch batch) {
		atlas = new TextureAtlas(Gdx.files.internal("data/WorldObjects/worldobjects.pack"));
		sprite = atlas.createSprite("ShadowBall");
		graphic = new SpriteGraphic(sprite);
		this.batch = batch;

	}
	
	public void draw(float delta, float x, float y) {
		graphic.setPosition(x, y);
		graphic.draw(delta, batch);
		
	}


	@Override
	public void dispose() {
		graphic.dispose();
	}

}
