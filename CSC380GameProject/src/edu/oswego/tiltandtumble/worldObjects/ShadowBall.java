package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.levels.UnitScale;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;

public class ShadowBall implements Disposable {

	private final GraphicComponent graphic;
	private final TextureAtlas atlas;
	private final UnitScale scale;
	
	public ShadowBall(GraphicComponent graphic, UnitScale scale) {
		atlas = new TextureAtlas(Gdx.files.internal("data/WorldObjects/worldobjects.pack"));
		this.scale = scale;
		
		this.graphic = graphic;

	}
	
	public void draw(float delta, float x, float y, SpriteBatch batch) {
		graphic.setPosition(x,y);
		graphic.draw(delta, batch);
	}

//	public float getRadius() {
//		return scale.metersToPixels(
//	//			body.getFixtureList().get(0).getShape().getRadius());
//	}
	@Override
	public void dispose() {
		graphic.dispose();
	}

}
