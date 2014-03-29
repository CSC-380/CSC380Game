package edu.oswego.tiltandtumble;

import java.util.ArrayList;
import java.util.List;

import edu.oswego.tiltandtumble.settings.SettingsObserver;
import edu.oswego.tiltandtumble.settings.SettingsUpdate;

public class Settings {
	public static enum Setting {
		USE_DPAD,
		DEBUG_RENDER,
		MUSIC,
		SOUND_EFFECTS
	}

	private final List<SettingsObserver> observers = new ArrayList<SettingsObserver>();

	private boolean useDpad;
	private boolean debugRender = false;
	private boolean music = false;
	private boolean soundEffect = false;

	public boolean isUseDpad() {
		return useDpad;
	}

	public void setUseDpad(boolean useDpad) {
		this.useDpad = useDpad;
		notifyObservers(new SettingsUpdate(Setting.USE_DPAD, useDpad));
	}

	public boolean isDebugRender() {
		return debugRender;
	}

	public void setDebugRender(boolean debugRender) {
		this.debugRender = debugRender;
		notifyObservers(new SettingsUpdate(Setting.DEBUG_RENDER, debugRender));
	}

	public boolean isMusicOn() {
		return music;
	}

	public void setMusic(boolean music) {
		this.music = music;
		notifyObservers(new SettingsUpdate(Setting.MUSIC, music));
	}

	public boolean isSoundEffectOn() {
		return soundEffect;
	}

	public void setSoundEffect(boolean soundEffect) {
		this.soundEffect = soundEffect;
		notifyObservers(new SettingsUpdate(Setting.SOUND_EFFECTS, soundEffect));
	}

	public void addObserver(SettingsObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(SettingsObserver observer) {
		observers.remove(observer);
	}

	void notifyObservers(SettingsUpdate update) {
		for (SettingsObserver o : observers) {
			o.handleSettingsChangeUpdate(update);
		}
	}
}
