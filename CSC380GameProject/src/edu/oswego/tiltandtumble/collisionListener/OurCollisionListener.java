package edu.oswego.tiltandtumble.collisionListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import edu.oswego.tiltandtumble.worldObjects.PushBumper;


public class OurCollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("begin contact", contact.toString());
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();
        if (a != null && b != null) {
            Gdx.app.log("begin contact", a.getClass().getName() + " > " + b.getClass().getName());
            if (a instanceof PushBumper) {
                ((PushBumper)a).handleCollision(contact, true);
            }
            else if (b instanceof PushBumper) {
                ((PushBumper)b).handleCollision(contact, false);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("end contact", contact.toString());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub
    }

}

