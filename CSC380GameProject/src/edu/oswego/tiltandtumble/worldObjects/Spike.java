package edu.oswego.tiltandtumble.worldObjects;

<<<<<<< HEAD
import com.badlogic.gdx.Gdx;
=======
import com.badlogic.gdx.assets.AssetManager;
>>>>>>> master
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.levels.Level;
import edu.oswego.tiltandtumble.worldObjects.graphics.GraphicComponent;

public class Spike extends AbstractWorldObject implements
		BallCollisionListener, Disposable, MapRenderable, Audible {
	public static final BodyType BODY_TYPE = BodyType.StaticBody;
	public static final boolean IS_SENSOR = false;

	private final Level level;
	private final GraphicComponent graphic;
	private boolean death = false;

	private boolean playSound;
	private final Sound sound;

<<<<<<< HEAD
	public Spike(Body body, Level level, GraphicComponent graphic) {
=======
	public Spike(Body body, Level level, GraphicComponent graphic, AssetManager assetManager) {
>>>>>>> master
		super(body);
		this.level = level;
		this.graphic = graphic;

		playSound = true;
<<<<<<< HEAD
		sound = Gdx.audio.newSound(Gdx.files.internal("data/soundfx/deflate.ogg"));
=======
		String soundFile = "data/soundfx/deflate.ogg";
		if (!assetManager.isLoaded(soundFile)) {
			assetManager.load(soundFile, Sound.class);
			assetManager.finishLoading();
		}
		sound = assetManager.get(soundFile, Sound.class);
>>>>>>> master
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		contact.setEnabled(false);
		level.fail();
		ball.hide();
		graphic.setSize(ball.getRadius() * 2, ball.getRadius() * 2);
		graphic.setPosition(ball.getMapX(), ball.getMapY());
		playSound();
		graphic.start();
		death = true;
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
	}

	@Override
	public void setPlaySound(boolean value) {
		playSound = value;
	}

	@Override
	public void playSound() {
		if (playSound) {
			sound.play();
		}
	}

	@Override
	public void endSound() {
		sound.stop();
	}

	@Override
	public void dispose() {
		graphic.dispose();
<<<<<<< HEAD
		sound.dispose();
=======
>>>>>>> master
	}

	@Override
	public void drawBeforeBall(float delta, SpriteBatch batch) {
	}

	@Override
	public void drawAfterBall(float delta, SpriteBatch batch) {
		if (death) {
			if (graphic.isFinished()) {
				level.exit();
			}
			graphic.draw(delta, batch);
		}
	}
}
