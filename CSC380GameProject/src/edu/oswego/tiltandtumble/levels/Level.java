package edu.oswego.tiltandtumble.levels;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

import edu.oswego.tiltandtumble.collisionListener.OurCollisionListener;
import edu.oswego.tiltandtumble.data.Score;
import edu.oswego.tiltandtumble.worldObjects.Ball;
import edu.oswego.tiltandtumble.worldObjects.MapRenderable;
import edu.oswego.tiltandtumble.worldObjects.WorldObject;
import edu.oswego.tiltandtumble.worldObjects.WorldUpdateable;

public class Level implements Disposable {

	private static enum State {
		NOT_STARTED,
		STARTED,
		FINISHED
	};

	// TODO: Setting default score to 1000, figure out a good value for this...
	private static final int DEFAULT_SCORE = 1000;

	private final World world = new World(new Vector2(0, 0), true);
	private final ContactListener contactListener;
	private final TiledMap map;
	private final Ball ball;
	private final BallController ballController;

	private final UnitScale scale = new UnitScale(1f/64f);

	private final int level;
	int mapPixelWidth;
	int mapPixelHeight;
	private State currentState;
	private boolean failed = false;

	private final Score score;
	//times are in milliseconds
	private long startTime;
	private final int baseScore;

	private final Collection<Disposable> disposableObjects;
	private final Collection<MapRenderable> renderableObjects;
	private final Collection<WorldUpdateable> updateableObjects;

	public Level(int level, BallController ballController, WorldPopulator populator) {
		this.level = level;
		this.ballController = ballController;


		currentState = State.NOT_STARTED;

		map = loadMap(level);
		MapProperties prop = map.getProperties();

		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;

		baseScore = map.getProperties().get("score", DEFAULT_SCORE, Integer.class);
		score = new Score(baseScore, 0);

		disposableObjects = new LinkedList<Disposable>();
		renderableObjects = new LinkedList<MapRenderable>();
		updateableObjects = new LinkedList<WorldUpdateable>();

		ball = populator.populateWorldFromMap(this, map, world, scale);
		this.ballController.setBall(ball);

		contactListener = new OurCollisionListener();
		world.setContactListener(contactListener);
	}

	public TiledMap getMap() {
		return map;
		
	}
	public int getMapWidth(){
		return mapPixelWidth;
	}
	public int getMapHeight(){
		return mapPixelHeight;
	}

	public World getWorld() {
		return world;
	}

	public UnitScale getScale() {
		return scale;
	}

	public Ball getBall() {
		return ball;
	}

	public Score getScore() {
		return score;
	}

	public boolean hasNotStarted() {
		return currentState == State.NOT_STARTED;
	}

	public void addWorldObject(WorldObject obj) {
		if (obj instanceof Disposable) {
			disposableObjects.add((Disposable)obj);
		}
		if (obj instanceof WorldUpdateable) {
			updateableObjects.add((WorldUpdateable)obj);
		}
		if (obj instanceof MapRenderable) {
			renderableObjects.add((MapRenderable)obj);
		}
		// we don't care about other object types at this time.
	}
	public void start() {
		if (currentState == State.NOT_STARTED) {
			currentState = State.STARTED;
		}
		startTime = TimeUtils.millis();
	}

	public boolean isStarted() {
		return currentState == State.STARTED;
	}

	public void finish() {
		finish(false);
	}

	public void finish(boolean fail) {
		if (currentState == State.STARTED) {
			currentState = State.FINISHED;
			updateScore();
			failed = fail;
		}
	}

	public boolean isFailed() {
		return failed;
	}

	public boolean hasFinished() {
		return currentState == State.FINISHED;
	}

	private void updateScore() {
		// the "difference" is the difference in seconds and for every 1 second the time elapses, 10 points will be taken off
		long difference = (TimeUtils.millis() - startTime) / 1000;
		if (failed || (baseScore - difference) < 0) {
			score.setPoints(0);
		}
		else {
			score.setPoints((int)(baseScore - difference));
		}
		score.setTime((int)difference);
	}

	private TiledMap loadMap(int level) {
		if (Gdx.files.internal("data/level" + level + ".tmx").exists()) {
			return new TmxMapLoader().load("data/level" + level + ".tmx");
		}
		throw new RuntimeException("data/level" + level + ".tmx does not exist");
	}

	public int getLevelNumber() {
		return level;
	}

	public void draw(SpriteBatch batch) {
		for (MapRenderable m : renderableObjects) {
			m.draw(batch);
		}
	}

	public void update() {
		if (currentState == State.STARTED) {
			ballController.update();
			for (WorldUpdateable w : updateableObjects) {
				w.update();
			}

			updateScore();

			// world.step(1/60f, 6, 2);
			world.step(1 / 45f, 10, 8);
		}
	}

	@Override
	public void dispose() {
		world.dispose();
		for (Disposable d : disposableObjects) {
			d.dispose();
		}
	}
}
