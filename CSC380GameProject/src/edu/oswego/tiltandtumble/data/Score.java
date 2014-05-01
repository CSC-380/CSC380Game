package edu.oswego.tiltandtumble.data;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable {
	private static final long serialVersionUID = 1L;

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
		return String.format("%02d:%02d",
				(int)Math.floor(time / 60),
				(time % 60));
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public int compareTo(Score score) {
		return this.points - score.points;
	}
}
