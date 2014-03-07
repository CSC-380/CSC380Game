package worldObjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;




public class PushBumper {
    Body body;

	public static final float SIZE = 1f;

	   public  Vector2 position = new Vector2();
	   public Rectangle bounds = new Rectangle();
	
	
	    public PushBumper(Vector2 pos) {

	        this.position = pos;
	        this.bounds.width = SIZE;
	        this.bounds.height = SIZE+1f;
	
	    }
	    
	    public Vector2 getPosition(){
	    	return position;
	    }

		public Rectangle getBounds() {

			return bounds;
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
		            // TODO: what i want is to be able to instantly accelerate the object
		            //       up to near maximum speed. the math for this could probably
		            //       be better. if we use the center of this object and the
		            //       outside point of contact to create a direction vector. we
		            //       could then use that to more accurately apply the force.
		            float force = 1000000.0f;
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
