package worldObjects;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;

public class Hole {
	public static final float SIZE = 1f;

	   //public  Vector2 position = new Vector2();
	   //public Circle bounds = new Circle();
	   CircleShape hole = new CircleShape();
	
	
	    public Hole(Vector2 pos) {

	        hole.setPosition(pos);
	        hole.setRadius(SIZE);
	  
	
	    }
	    
	    public Vector2 getPosition(){
	    	return hole.getPosition();
	    }

		public CircleShape getBounds() {

			return hole;
		}
}
