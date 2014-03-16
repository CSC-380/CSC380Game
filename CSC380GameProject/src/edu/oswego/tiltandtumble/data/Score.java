package edu.oswego.tiltandtumble.data;



public class Score implements Comparable<Score> {

	private final String initials;
	private final int points;
	private final int time;

	public Score(String initials, int points, int time) {
		this.initials = initials;
		this.points = points;
		this.time = time;
	}

	public String getInitials() {
		return initials;
	}

	public int getPoints() {
		return points;
	}

	public int getTime() {
		return time;
	}

	@Override
	public int compareTo(Score score) {
		return score.points - this.points;
	}
}