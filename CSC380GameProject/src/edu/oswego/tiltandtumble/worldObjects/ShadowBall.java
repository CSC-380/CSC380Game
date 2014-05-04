package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.levels.UnitScale;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;
import edu.oswego.tiltandtumble.worldObjects.graphics.SpriteGraphic;

public class ShadowBall implements Disposable {

	private Sprite sprite;
	//private SpriteBatch batch;
	Body body;
	GraphicComponent graphic;
	
	public ShadowBall(UnitScale scale, float x) {
		Texture shadow = new Texture(Gdx.files.internal("data/WorldObjects/ShadowOrb.png"));
		
		sprite = new Sprite(shadow);
		sprite.setSize(24.0f, 24.0f);
		sprite.setOrigin(12.0f, 12.0f);
		
		graphic = new SpriteGraphic(sprite);

	}
	
	public void draw(float delta, float x, float y) {
		//sprite.setPosition(x, y);
		graphic.setPosition(x, y);
		
	}
	public void drawFirst(float delta, SpriteBatch batch) {
		//sprite.draw(batch);
		graphic.draw(delta, batch);
	}


	@Override
	public void dispose() {
		//graphic.dispose();
	}

}
