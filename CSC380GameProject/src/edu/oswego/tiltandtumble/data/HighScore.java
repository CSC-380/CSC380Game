package edu.oswego.tiltandtumble.data;

import java.io.Serializable;
import java.util.Date;

public class HighScore extends Score implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String initials;
	private final Date saved;

	public HighScore(String initials, int points, int time) {
		super(points, time);
		this.initials = initials;
		saved = new Date();
	}

	public HighScore(String initials, Score score) {
		this(initials, score.getPoints(), score.getTime());
	}

	public String getInitials() {
		return initials;
	}

	public Date getDate() {
		return saved;
	}
}
