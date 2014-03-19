package edu.oswego.tiltandtumble.data;

public class Score implements Comparable<Score> {

	private int points;
	private int time;

	public Score(int points, int time) {
		this.points = points;
		this.time = time;
	}

	public int getPoints() {
		return points;
	}

	public int getTime() {
		return time;
	}

	public String getFormattedTime() {
		return (int)Math.floor(time / 60) + ":" + (time % 60);
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public int compareTo(Score score) {
		return score.points - this.points;
	}
}
