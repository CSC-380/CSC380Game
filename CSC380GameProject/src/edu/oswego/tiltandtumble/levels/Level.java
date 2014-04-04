package edu.oswego.tiltandtumble.levels;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

import edu.oswego.tiltandtumble.collisionListener.OurCollisionListener;
import edu.oswego.tiltandtumble.data.Score;
import edu.oswego.tiltandtumble.worldObjects.Audible;
import edu.oswego.tiltandtumble.worldObjects.Ball;
import edu.oswego.tiltandtumble.worldObjects.MapRenderable;
import edu.oswego.tiltandtumble.worldObjects.WorldObject;
import edu.oswego.tiltandtumble.worldObjects.WorldUpdateable;

public class Level implements Disposable {

	// TODO: Setting default score to 1000, figure out a good value for this...
	private static final int DEFAULT_SCORE = 1000;

	private final World world = new World(new Vector2(0, 0), true);
	private final ContactListener contactListener;
	private final TiledMap map;
	private final Ball ball;
	private final BallController ballController;

	// NOTE: 1/64 means 1px end up being about 1.6cm in world physics
	private final UnitScale scale = new UnitScale(1f/64f);

	private final int level;
	private boolean failed = false;
	private State currentState;

	private final Score score;
	//times are in milliseconds
	private long startTime;
	private final int baseScore;

	private final int mapWidth;
	private final int mapHeight;

	private final Collection<Disposable> disposableObjects;
	private final Collection<MapRenderable> renderableObjects;
	private final Collection<WorldUpdateable> updateableObjects;
	private final Collection<Audible> audibleObjects;

	public Level(int level, BallController ballController, WorldPopulator populator) {
		this.level = level;
		this.ballController = ballController;

		currentState = new NotStarted();

		map = loadMap(level);

		mapWidth = map.getProperties().get("width", Integer.class)
				* map.getProperties().get("tilewidth", Integer.class);
		mapHeight = (map.getProperties().get("height", Integer.class)
				* map.getProperties().get("tileheight", Integer.class))
				+ 32; // adding extra for HUD

		baseScore = map.getProperties().get("score", DEFAULT_SCORE, Integer.class);
		score = new Score(baseScore, 0);

		disposableObjects = new LinkedList<Disposable>();
		renderableObjects = new LinkedList<MapRenderable>();
		updateableObjects = new LinkedList<WorldUpdateable>();
		audibleObjects = new LinkedList<Audible>();

		ball = populator.populateWorldFromMap(this, map, world, scale);
		this.ballController.setBall(ball);

		contactListener = new OurCollisionListener();
		world.setContactListener(contactListener);
	}

	public TiledMap getMap() {
		return map;
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

	public BallController getBallController() {
		return ballController;
	}

	public Collection<Audible> getAudibles() {
		return audibleObjects;
	}

	public boolean hasNotStarted() {
		return currentState instanceof NotStarted;
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
		if (obj instanceof Audible) {
			audibleObjects.add((Audible)obj);
		}
		// we don't care about other object types at this time.
	}

	public void start() {
		currentState.start();
	}

	public boolean isStarted() {
		return currentState instanceof Started;
	}

	public void finish() {
		finish(false);
	}

	public void finish(boolean fail) {
		currentState.finish(fail);
	}

	public boolean isFailed() {
		return failed;
	}

	public boolean hasFinished() {
		return currentState instanceof Finished;
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

	public void draw(float delta, SpriteBatch batch) {
		for (MapRenderable m : renderableObjects) {
			m.draw(delta, batch);
		}
	}

	public void update(float delta) {
		currentState.update(delta);
	}

	private boolean isBallOutsideLevel() {
		return ball.getMapX() < 0
				|| ball.getMapX() > mapWidth
				|| ball.getMapY() < 0
				|| ball.getMapY() > mapHeight;
	}

	@Override
	public void dispose() {
		world.dispose();
		for (Disposable d : disposableObjects) {
			d.dispose();
		}
	}

	public void pause() {
		currentState.pause();
	}

	public void resume() {
		currentState.resume();
	}

	private abstract class State {
		public void start() {
		}

		public void pause() {
		}

		public void resume() {
		}

		public void finish(boolean fail) {
		}

		public void update(float delta) {
		}
	}

	private class NotStarted extends State {
		@Override
		public void start() {
			startTime = TimeUtils.millis();
			currentState = new Started();
		}
	}

	private class Started extends State {
		@Override
		public void pause() {
			currentState = new Paused();
		}

		@Override
		public void finish(boolean fail) {
			failed = fail;
			updateScore();
			currentState = new Finished();
		}

		@Override
		public void update(float delta) {
			if (isBallOutsideLevel()) {
				finish(true);
			}
			ballController.update(delta);
			for (WorldUpdateable w : updateableObjects) {
				w.update(delta);
			}

			updateScore();

			// world.step(1/60f, 6, 2);
			world.step(1 / 45f, 10, 8);
		}
	}

	private class Paused extends State {
		private final long pauseTime = TimeUtils.millis();

		@Override
		public void resume() {
			startTime += (TimeUtils.millis() - pauseTime);
			currentState = new Started();
		}
	}

	private class Finished extends State {
	}
}
