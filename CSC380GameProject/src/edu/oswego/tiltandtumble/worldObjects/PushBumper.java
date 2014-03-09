package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;

import edu.oswego.tiltandtumble.levels.UnitScale;

public class PushBumper extends AbstractWorldObject {
    public static final float FRICTION = 0.0f;
    public static final float DENSITY = 2.0f;
    public static final float RESTITUTION = 0.0f;
    public static final BodyType BODY_TYPE = BodyType.StaticBody;

    private final UnitScale scale;

    public PushBumper(Body body, UnitScale scale) {
        super(body);
        this.scale = scale;
        body.setUserData(this);
    }

    public void handleCollision(Contact contact, boolean isA) {
        Gdx.app.log("push bumper", "PUSH!!!!");
        Body target;
        if (isA) {
            target = contact.getFixtureB().getBody();
        }
        else {
            target = contact.getFixtureA().getBody();
        }
        Object type = target.getUserData();
        if (type instanceof Ball) {
            Gdx.app.log("push bumper", "PUSH Ball!!!!");
            // TODO: what i want is to be able to instantly accelerate the object
            //       up to near maximum speed. the math for this could probably
            //       be better. if we use the center of this object and the
            //       outside point of contact to create a direction vector. we
            //       could then use that to more accurately apply the force.
            //       not sure about the math to determine how much force to apply.
            float force = 0.5f;
            target.applyLinearImpulse(
                force * target.getLinearVelocity().x,
                force * target.getLinearVelocity().y,
                // FIXME: what point do i need to use to not create a lot of spin?
                target.getLocalCenter().x,
                target.getLocalCenter().y,
                //target.getPosition().x,
                //target.getPosition().y,
                true);
        }
    }
}
