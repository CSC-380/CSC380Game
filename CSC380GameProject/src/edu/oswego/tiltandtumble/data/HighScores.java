package edu.oswego.tiltandtumble.data;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class HighScores implements Serializable{
	private static final long serialVersionUID = -2777200203575485944L;
	private static final String FILE = "Scores.dat";

	private final SortedSet<Score> scores;

	public HighScores() {
		scores = new TreeSet<Score>();
	}

	public void compareAndSave(Score score) {
		scores.add(score);
		if (scores.size() > 10) {
			scores.remove(scores.first());
		}
	}

	public static void save(HighScores scores) {
		FileHandle file = Gdx.files.local(FILE);
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(file.write(false));
			out.writeObject(scores);
		} catch (IOException e) {
			Gdx.app.log("HighScores", e.getMessage(), e);
		}
		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Gdx.app.log("HighScores", e.getMessage(), e);
				}
			}
		}
	}

	public static HighScores load() {
		FileHandle file = Gdx.files.local("Scores.dat");
		HighScores scores = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(file.read());
			scores = (HighScores)in.readObject();
		} catch (Exception e) {
			Gdx.app.log("HighScores", e.getMessage(), e);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Gdx.app.log("HighScores", e.getMessage(), e);
				}
			}
		}
		if (scores == null) {
			scores = new HighScores();
		}
		return scores;
	}
}
