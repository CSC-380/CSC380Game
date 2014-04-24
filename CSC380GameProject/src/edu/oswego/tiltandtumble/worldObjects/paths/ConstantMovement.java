package edu.oswego.tiltandtumble.worldObjects.paths;

import com.badlogic.gdx.math.Vector2;

public class ConstantMovement implements MovementStrategy {
	private boolean isActive = true;
	private final float speed;
	private final PathPointTraverser nodes;

	private final Vector2 startNode = new Vector2();
	private final Vector2 endNode = new Vector2();

	public ConstantMovement(PathPointTraverser nodes, float speed) {
		this.nodes = nodes;
		this.speed = speed;
	}

	@Override
	public void activate() {
		isActive = true;
	}

	@Override
	public void deactivate() {
		isActive = false;
	}

	@Override
	public Vector2 move(Vector2 start, float timeDelta) {
		if (!isActive) return start;
		startNode.set(start);
		float distanceToTravel = speed * timeDelta;
		float distanceToPoint = start.dst(nodes.current().x, nodes.current().y);
		// move along the path until we find the segment we need to stop on
		while (distanceToTravel > distanceToPoint) {
			distanceToTravel = distanceToTravel - distanceToPoint;
			startNode.set(nodes.current().x, nodes.current().y);

			nodes.next();
			distanceToPoint = start.dst(nodes.current().x, nodes.current().y);
		}
		endNode.set(nodes.current().x, nodes.current().y);

		// now we have a valid distance on the next line segment.
		// C = A + k(B - A)
		final float distance = distanceToTravel / distanceToPoint;
		return endNode.sub(startNode).scl(distance).add(startNode);
	}
}
