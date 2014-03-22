package edu.oswego.tiltandtumble.worldObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.levels.UnitScale;

public class MovingWall extends AbstractWorldObject
		implements MapRenderable, WorldUpdateable, Disposable {
	public static final float FRICTION = 0.5f;
	public static final float DENSITY = 5.0f;
	public static final float RESTITUTION = 0.0f;
	public static final BodyType BODY_TYPE = BodyType.KinematicBody;

	// in meters per second
	public static final float DEFAULT_SPEED = 1f;
	public static final String DEFAULT_SPRITE = "WallGrey2x2.png";

	private final float speed;
	private final PathTraverser nodes;
	private final UnitScale scale;

	private final Texture texture;
	private final Sprite sprite;

	public MovingWall(Body body, float speed, PathPoint path,
			String spriteName, Vector2 dimensions, UnitScale scale) {
		super(body);

		this.speed = speed;
		this.scale = scale;

		nodes = new PathTraverser(path);
		nodes.next();

		texture = new Texture(Gdx.files.internal("data/" + spriteName));
		sprite = new Sprite(texture);
		sprite.setSize(dimensions.x, dimensions.y);
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.setPosition(getMapX(), getMapY());
		sprite.draw(batch);
	}

	public float getMapX() {
		return scale.metersToPixels(body.getPosition().x) - (sprite.getWidth() * 0.5f);
	}

	public float getMapY() {
		return scale.metersToPixels(body.getPosition().y) - (sprite.getHeight() * 0.5f);
	}

	@Override
	public void update(float delta) {
		// TODO: this can be cleaned up some using the functions on the Vector2 class
		Vector2 start = body.getPosition();
		float distanceToTravel = speed * delta;
		float distanceToPoint = getDistanceToNextPoint(nodes.current());
		// move along the path until we find the segment we need to stop on
		while (distanceToTravel > distanceToPoint) {
			distanceToTravel = distanceToTravel - distanceToPoint;
			start.x = nodes.current().x;
			start.y = nodes.current().y;
			nodes.next();
			distanceToPoint = getDistanceToNextPoint(nodes.current());
		}
		// now we have a valid distance on the next line segment.
		// C = A + k(B - A)
		final float distance = distanceToTravel / distanceToPoint;
		body.setTransform(
				start.x + (distance * (nodes.current().x - start.x)),
				start.y + (distance * (nodes.current().y - start.y)),
				0);

	}

	private float getDistanceToNextPoint(PathPoint point) {
		float x1 = body.getPosition().x;
		float y1 = body.getPosition().y;
		float x2 = point.x;
		float y2 = point.y;

		return (float)Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	private static class PathTraverser {
		private boolean forward = true;
		private PathPoint current;

		PathTraverser(PathPoint node) {
			current = node;
		}

		PathPoint current() {
			return current;
		}

		PathPoint next() {
			if (forward) {
				if (current.hasNext()) {
					current = current.getNext();
				} else {
					forward = !forward;
					current = current.getPrevious();
				}
			} else {
				if (current.hasPrevious()) {
					current = current.getPrevious();
				} else {
					forward = !forward;
					current = current.getNext();
				}
			}
			return current;
		}
	}

	@Override
	public void dispose() {
		texture.dispose();
	}
}
