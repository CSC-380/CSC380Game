package edu.oswego.tiltandtumble.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.OurCollisionListener;
import edu.oswego.tiltandtumble.worldObjects.Ball;

public class Level implements Disposable {

	public static enum State {
		NOT_STARTED,
		STARTED,
		FINISHED
	};

	private final World world = new World(new Vector2(0, 0), true);
	private final ContactListener contactListener;
	private final TiledMap map;
	private final Ball ball;
	private final BallController ballController;

	private final UnitScale scale = new UnitScale(1f/64f);

	private final int level;
	private State currentState;

	public Level(int level, BallController ballController, WorldPopulator populator) {
		this.level = level;
		this.ballController = ballController;

		currentState = State.NOT_STARTED;

		// TODO: allow for map properties to be used to customize the level
		//       behavior if needed...
		map = loadMap(level);

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

	public boolean hasNotStarted() {
		return currentState == State.NOT_STARTED;
	}

	public void start() {
		if (currentState == State.NOT_STARTED) {
			currentState = State.STARTED;
		}
	}

	public boolean isStarted() {
		return currentState == State.STARTED;
	}

	public void finish() {
		if (currentState == State.STARTED) {
			currentState = State.FINISHED;
		}
	}

	public boolean hasFinished() {
		return currentState == State.FINISHED;
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
		ball.draw(batch);
	}

	public void update() {
		if (currentState == State.STARTED) {
			ballController.update();

			// world.step(1/60f, 6, 2);
			world.step(1 / 45f, 10, 8);
		}
	}

	@Override
	public void dispose() {
		world.dispose();
		ball.dispose();
	}
}
