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
	private final PathPointTraverser nodes;
	private final UnitScale scale;

	private final Texture texture;
	private final Sprite sprite;

	private final Vector2 startNode = new Vector2();
	private final Vector2 endNode = new Vector2();

	public MovingWall(Body body, float speed, PathPointTraverser nodes,
			String spriteName, Vector2 dimensions, UnitScale scale) {
		super(body);

		this.speed = speed;
		this.scale = scale;

		this.nodes = nodes;
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
		startNode.set(body.getPosition());
		float distanceToTravel = speed * delta;
		float distanceToPoint = body.getPosition()
				.dst(nodes.current().x, nodes.current().y);
		// move along the path until we find the segment we need to stop on
		while (distanceToTravel > distanceToPoint) {
			distanceToTravel = distanceToTravel - distanceToPoint;
			startNode.set(nodes.current().x, nodes.current().y);

			nodes.next();
			distanceToPoint = body.getPosition()
					.dst(nodes.current().x, nodes.current().y);
		}
		endNode.set(nodes.current().x, nodes.current().y);

		// now we have a valid distance on the next line segment.
		// C = A + k(B - A)
		final float distance = distanceToTravel / distanceToPoint;
		body.setTransform(
				endNode.sub(startNode).scl(distance).add(startNode), 0);
	}

	@Override
	public void dispose() {
		texture.dispose();
	}
}
