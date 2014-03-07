package edu.oswego.tiltandtumble.levels;





import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import edu.oswego.tiltandtumble.collisionListener.OurCollisionListener;
import edu.oswego.tiltandtumble.worldObjects.*;


public class WorldRenderer extends OrthogonalTiledMapRenderer{
	private WorldPopulator worldPopulator;
	World word;
	//World
	private OrthographicCamera cam;

	/** Textures **/
	private Texture ballTexture;
	private Texture blockTexture;
	private Texture holeTexture;
	private Texture movingWallTexture;
	private Texture pushBumperTexture;
	private float ppuX; // pixels per unit on the X axis
	private float ppuY; // pixels per unit on the Y axis
	static TiledMap map = new TiledMap();
	//want explanation of this
	World world;
	//OurCollisionListener collisionListener;
	private SpriteBatch spriteBatch;
	private boolean debug = false;
	private int width;//480
	private int height ;//320
	private float oldBallX;
	private float oldBallY;
	private float cameraWidth = 320;
	private float cameraHeight = 480;
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
		ppuX = (float)width / cam.viewportWidth;
		 ppuY = (float)height / cam.viewportHeight;


	}

	public WorldRenderer(WorldPopulator worldPopulator, boolean debug) {
		super(map,1);
		this.worldPopulator = worldPopulator;
		world = worldPopulator.getWorld();
		debugRenderer.setDrawVelocities(true);
        debugRenderer.setDrawContacts(true);
        debugRenderer.setDrawJoints(true);
        debugRenderer.setDrawBodies(true);
        
    	width = worldPopulator.getLevel().getWidth();//480
    	height = worldPopulator.getLevel().getHeight();//320
		
    	this.cam = new OrthographicCamera(cameraWidth, cameraHeight);
		cam.position.set(cameraWidth/2, cameraHeight/2, 0);
		this.setView(cam);
		ppuX = width / cam.viewportWidth;
		 ppuY = height / cam.viewportHeight;
		 cameraWidth = cam.viewportWidth;
		 cameraHeight = cam.viewportHeight;
		 System.out.println("cam width " + cameraWidth + " ppux " + ppuX);
		 System.out.println("cam height " + cameraHeight + " ppuy " + ppuY);
		debugRenderer.render(world, cam.combined);
		
		oldBallX = worldPopulator.getBall().getPosition().x;
		oldBallY = worldPopulator.getBall().getPosition().y;
		this.debug = true;
		
		spriteBatch = new SpriteBatch();
		loadTextures();
	}

	private void loadTextures() {
		ballTexture =  new Texture(Gdx.files.internal("data/GreenOrb.png"));
		worldPopulator.getBallSprite().setTexture(ballTexture);
		blockTexture = new Texture(Gdx.files.internal("data/Brick_block.png"));
		
		pushBumperTexture = new Texture(Gdx.files.internal("data/bumper.png"));
		holeTexture = new Texture(Gdx.files.internal("data/Hole.png"));
		//add textures for other objects
	}
    @Override
	public void render() {
    	cam.update();
    	spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();
			//drawBlocks();
			//drawBall();
			//drawHole();
			worldPopulator.render(spriteBatch);
		spriteBatch.end();
		this.moveCam();
		//world.step(1/45f, 10, 8);
		if (debug)
			debugRenderer.render(world, cam.combined);
		
	}
    
    private void moveCam(){
 

    	if(worldPopulator.getBall().getPosition().x*ppuX > (cameraWidth/2) && worldPopulator.getBall().getPosition().y*ppuY > (cameraHeight/2)){
    		if(worldPopulator.getBall().getPosition().x*ppuX >= cam.position.x && worldPopulator.getBall().getPosition().y*ppuY>= cam.position.y ){ 		
    			cam.position.x = worldPopulator.getBall().getPosition().x*ppuX;
    			cam.position.y = worldPopulator.getBall().getPosition().y*ppuY;

    		}else if((worldPopulator.getBall().getPosition().x*ppuX <= cam.position.x) && (worldPopulator.getBall().getPosition().y*ppuY <= cam.position.y)){
    			cam.position.x = worldPopulator.getBall().getPosition().x*ppuX;
    			cam.position.y = worldPopulator.getBall().getPosition().y*ppuY;

    		}else if(worldPopulator.getBall().getPosition().x*ppuX <= cam.position.x ){
    			cam.position.x = worldPopulator.getBall().getPosition().x*ppuX;

    		}else if(worldPopulator.getBall().getPosition().y*ppuY <= cam.position.y){
    			cam.position.y = worldPopulator.getBall().getPosition().y*ppuY;

    		}  
    	}else if(worldPopulator.getBall().getPosition().x*ppuX > (cameraWidth/2) && worldPopulator.getBall().getPosition().y*ppuY < (cameraHeight/2)){
    			cam.position.x = worldPopulator.getBall().getPosition().x*ppuX;

    	}else if(worldPopulator.getBall().getPosition().y*ppuY > (cameraHeight/2) && worldPopulator.getBall().getPosition().x*ppuX< (cameraWidth/2)){
    			cam.position.y = worldPopulator.getBall().getPosition().y*ppuY;
    	}

    }

    

	private void drawBlocks() {
	
		for (Block block : worldPopulator.getDrawableBlocks((int)cameraWidth, (int)cameraHeight)) {
			spriteBatch.draw(blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
	
		}
	}

	private void drawBall() {
		Ball ball = worldPopulator.getBall();
		spriteBatch.draw(ballTexture, ball.getPosition().x * ppuX, ball.getPosition().y * ppuY, Ball.SIZE * ppuX, Ball.SIZE * ppuY);
	}
	
	
	private void drawHole(){
		for (Hole hole : worldPopulator.getDrawableHoles((int)cameraWidth, (int)cameraHeight)) {
			spriteBatch.draw(holeTexture, hole.getPosition().x * ppuX, hole.getPosition().y * ppuY , Hole.SIZE * ppuX , Hole.SIZE *ppuY);
		}
	}

	
}
