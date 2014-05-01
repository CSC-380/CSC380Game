package edu.oswego.tiltandtumble.worldObjects.paths;

import com.badlogic.gdx.math.Vector2;

public class NodeStopMovement implements MovementStrategy {
	private boolean isMoving = true;
	private final float speed;
	private final PathPointTraverser nodes;

	private final Vector2 startNode = new Vector2();
	private final Vector2 endNode = new Vector2();

	public NodeStopMovement(PathPointTraverser nodes, float speed) {
		this.nodes = nodes;
		this.speed = speed;
	}

	@Override
	public void activate() {
		if (!isMoving) {
			nodes.next();
			isMoving = true;
		}
	}

	@Override
	public void deactivate() {
		if (!isMoving) {
			nodes.next();
			isMoving = true;
		}
	}

	@Override
	public Vector2 move(Vector2 start, float timeDelta) {
		if (!isMoving) return start;
		startNode.set(start);
		float distanceToTravel = speed * timeDelta;
		float distanceToPoint = start.dst(nodes.current().x, nodes.current().y);
		endNode.set(nodes.current().x, nodes.current().y);

		if (distanceToTravel > distanceToPoint) {
			isMoving = false;
			return endNode;
		}
		else {
			// now we have a valid distance on the next line segment.
			// C = A + k(B - A)
			final float distance = distanceToTravel / distanceToPoint;

			Vector2 end = endNode.sub(startNode).scl(distance).add(startNode);
			return end;
		}
	}
}
