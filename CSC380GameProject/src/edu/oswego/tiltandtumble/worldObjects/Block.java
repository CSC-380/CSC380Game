package edu.oswego.tiltandtumble.worldObjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import edu.oswego.tiltandtumble.collisionListener.OurCollisionListener;





public class Block {

	public static final float SIZE = 1f;

	   public  Vector2 position = new Vector2();
	 //  public Rectangle bounds = new Rectangle();
	   //PolygonShape block = new PolygonShape();
	   RectangleMapObject recMapObj = new RectangleMapObject();
	  Rectangle rec;
	   
	
	
	    public Block(Vector2 pos) {

	    	this.position = pos;
	    	rec = recMapObj.getRectangle();
			recMapObj.setColor(new Color(1,0,1,1));
			rec.setSize(SIZE);
			rec.setPosition(pos);
      
	    }
	    
	    
	    public Vector2 getPosition(){
	    	return position;
	    }
	
		public RectangleMapObject getBlock(){
			
			return recMapObj;
		}
	    
		 public void handleCollision(Contact contact, boolean isA) {
		        Gdx.app.log("block", "STOP");
		        Fixture target;
		        if (isA) {
		            target = contact.getFixtureB();
		        }
		        else {
		            target = contact.getFixtureA();
		        }
		        Object type = target.getUserData();
		        if (type instanceof Ball) {
		            target.getBody().setLinearVelocity(new Vector2(0,0));
		        }	
}
}
