package levels;


import java.util.ArrayList;

import collisionListener.OurCollisionListener;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import worldObjects.*;


public final class WorldPopulator{
//our world

	Ball ball;
	Block block;
	Levels level;
	OurCollisionListener collisionListener;
	int levelNum;
	World world;
	Body ballBody;
	Sprite ballSprite;
	Body blockBody;
	Sprite blockSprite;
	private float ppuX; // pixels per unit on the X axis
	private float ppuY; // pixels per unit on the Y axis
	ArrayList<Body> blocks2 = new ArrayList<Body>();

	
	
	public WorldPopulator(int levelNum) {
		this.levelNum = levelNum;
		createWorld();
		createBall();
		createBlocks();
		ballSprite = new Sprite();
		ballSprite.setScale(2);	
		
	}
	private void createWorld() {
		ball = new Ball(new Vector2(3, 3));
		level = new Levels(levelNum);
		ppuX = level.getWidth() / 480;
		ppuY = level.getHeight() / 320;
		world = new World(new Vector2(0,0),false);
		collisionListener = new OurCollisionListener();
		world.setContactListener(collisionListener);
		
	}
	
	private void createBall(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(ball.getPosition());
		ballBody = world.createBody(bodyDef);
		ballBody.setUserData(ball);
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setPosition(ball.getPosition());
		shape.setRadius((1/2)*ppuX);          
        fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.restitution = 0.7f;
		ballBody.createFixture(fixtureDef);
		ballBody.setAngularDamping(1);
	}
	private void createBlocks(){
		BodyDef bdef = new BodyDef();
        bdef.type = BodyType.StaticBody;
		Block block;
		for (int col = 0; col < level.getWidth(); col++) {
			for (int row = 0; row < level.getHeight(); row++) {
				block = level.getBlocks()[col][row];
				if (block != null) {
					bdef.position.set(block.getPosition());
		        	blockBody = world.createBody(bdef);
		        	blockBody.setUserData(block);
		        	FixtureDef fdef = new FixtureDef();
		        	PolygonShape shape = new PolygonShape();
		        	shape.setAsBox(1*ppuX,1*ppuY);
		            fdef.shape = shape;
		            fdef.density = 5.0f;
		            fdef.restitution = 0.0f;
		            blockBody.createFixture(fdef);

				
				}
			}
		}
		
	}
	

	

	public Ball getBall() {
		return ball;
	}
	public Levels getLevel() {
		return level;
	}
	public World getWorld(){
		return world;
	}
	
	public Body getBallBody(){
		return ballBody;
		
	}
	public Sprite getBallSprite(){
		return ballSprite;
	}
	public void render(SpriteBatch batch){
		ballSprite.setPosition(ball.getPosition().x, ball.getPosition().y);
	    ballSprite.draw(batch);
	}

	
	/** Return only the blocks that need to be drawn **/
	public ArrayList<Block> getDrawableBlocks(int width, int height) {
		int x = (int)ball.getPosition().x - width;
		int y = (int)ball.getPosition().y - height;
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		int x2 = x + 2 * width;
		int y2 = y + 2 * height;
		if (x2 > level.getWidth()) {
			x2 = level.getWidth() - 1;
		}
		if (y2 > level.getHeight()) {
			y2 = level.getHeight() - 1;
		}

		ArrayList<Block> blocks = new ArrayList<Block>();
		Block block;
		for (int col = x; col <= x2; col++) {
			for (int row = y; row <= y2; row++) {
				block = level.getBlocks()[col][row];
				if (block != null) {
					blocks.add(block);
				}
			}
		}
		
		return blocks;
	}
	
	public ArrayList<Hole> getDrawableHoles(int width, int height) {
		int x = (int)ball.getPosition().x - width;
		int y = (int)ball.getPosition().y - height;
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		int x2 = x + 2 * width;
		int y2 = y + 2 * height;
		if (x2 > level.getWidth()) {
			x2 = level.getWidth() - 1;
		}
		if (y2 > level.getHeight()) {
			y2 = level.getHeight() - 1;
		}

		ArrayList<Hole> holes = new ArrayList<Hole>();
		Hole hole;
		for (int col = x; col <= x2; col++) {
			for (int row = y; row <= y2; row++) {
				hole = level.getHoles()[col][row];
				if (hole != null) {
					holes.add(hole);
				}
			}
		}
		return holes;
	}



	
}
	
	


