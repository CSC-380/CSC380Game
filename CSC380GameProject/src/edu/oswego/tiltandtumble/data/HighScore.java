package edu.oswego.tiltandtumble.data;

public class HighScore extends Score {

	private final String initials;

	public HighScore(String initials, int points, int time) {
		super(points, time);
		this.initials = initials;
	}

	public String getInitials() {
		return initials;
	}
}
