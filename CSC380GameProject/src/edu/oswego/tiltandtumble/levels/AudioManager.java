package edu.oswego.tiltandtumble.levels;

import java.util.Collection;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.settings.Settings.Setting;
import edu.oswego.tiltandtumble.settings.SettingsObserver;
import edu.oswego.tiltandtumble.settings.SettingsUpdate;
import edu.oswego.tiltandtumble.worldObjects.Audible;

public class AudioManager implements SettingsObserver, Disposable {

	private final Collection<Audible> audibles;
	private boolean playMusic;
	private final Music music;
	private final AssetManager assetManager;
	private final String filename;

	public AudioManager(Level level, boolean playMusic, boolean playEffects,
			AssetManager assetManager) {
		audibles = level.getAudibles();
		this.assetManager = assetManager;

		setPlayMusic(playMusic);
		setPlayEffects(playEffects);

		String file = level.getMap().getProperties().get("music", String.class);
		if (file != null) {
			filename = "data/music/" + file;
			if (!assetManager.isLoaded(filename)) {
				assetManager.load(filename, Music.class);
				assetManager.finishLoading();
			}
			music = assetManager.get(filename, Music.class);
		} else {
			filename = null;
			music = null;
		}
	}

	public void start() {
		if (playMusic && music != null) {
			music.play();
			music.setLooping(true);
		}
	}

	public void pause() {
		if (music != null) {
			music.pause();
		}
	}

	public void setPlayMusic(boolean value) {
		playMusic = value;
		if (music != null && music.isPlaying()) {
			music.stop();
		}
	}

	public void setPlayEffects(boolean value) {
		for (Audible a : audibles) {
			a.setPlaySound(value);
		}
	}

	@Override
	public void dispose() {
		if (music != null) {
			assetManager.unload(filename);
		}
	}

	@Override
	public void handleSettingsChangeUpdate(SettingsUpdate update) {
		if (update.getSetting() == Setting.MUSIC) {
			setPlayMusic(update.getValue());
		}
		else if (update.getSetting() == Setting.SOUND_EFFECTS) {
			setPlayEffects(update.getValue());
		}
	}
}
