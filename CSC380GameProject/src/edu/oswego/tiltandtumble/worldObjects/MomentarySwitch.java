package edu.oswego.tiltandtumble.worldObjects;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;

public class MomentarySwitch extends AbstractWorldObject implements
	BallCollisionListener, Switch  {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = true;

	private final Collection<Activatable> activatables;

	public MomentarySwitch(Body body) {
		super(body);
		activatables = new LinkedList<Activatable>();
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		for (Activatable a : activatables) {
			a.activate();
		}
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
		for (Activatable a : activatables) {
			a.deactivate();
		}
	}

	@Override
	public void addActivatable(Activatable a) {
		activatables.add(a);
	}
}
