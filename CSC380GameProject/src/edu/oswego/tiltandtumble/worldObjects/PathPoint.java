package edu.oswego.tiltandtumble.worldObjects;


public class PathPoint {

	public final float x;
	public final float y;

	private PathPoint next;
	private PathPoint previous;

	public PathPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setNext(PathPoint next) {
		this.next = next;
	}

	public void setPrevious(PathPoint previous) {
		this.previous = previous;
	}

	public PathPoint getNext() {
		return next;
	}

	public PathPoint getPrevious() {
		return previous;
	}

	public boolean hasNext() {
		return next != null;
	}

	public boolean hasPrevious() {
		return previous != null;
	}
}
