package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;

public class MomentarySwitch extends ToggleSwitch {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	public MomentarySwitch(Body body, boolean startOn, GraphicComponent graphicOn,

		GraphicComponent graphicOff, AssetManager assetManager) {
		super(body, startOn, graphicOn, graphicOff, assetManager);
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
		currentState.toggle(this);
	}
}
