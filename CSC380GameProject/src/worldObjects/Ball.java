package worldObjects;



import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;


public class Ball {



	public static final float SPEED = 4f;	// unit per second
	public static final float SIZE = 0.5f; // half a unit
	BodyDef bodyDef = new BodyDef();
	
	
	Vector2 	acceleration = new Vector2();
	Vector2 	velocity = new Vector2();
	Circle ball;
	CircleMapObject cirMapObj = new CircleMapObject();

	public Ball(Vector2 position) {
		ball = cirMapObj.getCircle();
		cirMapObj.setColor(new Color(1,0,1,1));
		ball.setPosition(position);
		ball.setRadius(SIZE/2);
		bodyDef.type = BodyType.DynamicBody;
	
	
	}
	public BodyDef getBodyDef(){
		return bodyDef;
	}
	 
	public void update(float delta) {
		float x = ball.x;
		float y = ball.y;
		Vector2 pos = new Vector2(x,y);
		pos.add(velocity.cpy().scl(delta));
		ball.setY(pos.x);
		ball.setY(pos.y);
	}

	public Vector2 getPosition(){
		float x = ball.x;
		float y = ball.y;	
		return new Vector2(x,y);
		
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public Vector2 getVelocity() {
	
		return velocity;
	}
	
	public void setPosition(Vector2 position) {
		ball.setPosition(position);
	}


	public void setAcceleration(Vector2 acceleration) {
		this.acceleration = acceleration;
	}


	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	public Circle getBall(){
		return ball;
	}
	public void stop(){
		
	}
	
}
