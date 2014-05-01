package edu.oswego.tiltandtumble.worldObjects.paths;


public class PathPointTraverser {
	private boolean forward = true;
	private PathPoint current;

	public PathPointTraverser(PathPoint node, boolean forward) {
		current = node;
		this.forward = forward;
		if (forward) {
			next();
		}
	}

	public PathPointTraverser(PathPoint node) {
		this(node, true);
	}

	public PathPoint current() {
		return current;
	}

	public PathPoint next() {
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
