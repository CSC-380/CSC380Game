package edu.oswego.tiltandtumble.settings;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Preferences;


public class Settings {
	public static enum Setting {
		USE_DPAD, DEBUG_RENDER, MUSIC, SOUND_EFFECTS
	}

	private final Preferences prefs;
	private final List<SettingsObserver> observers = new ArrayList<SettingsObserver>();

	public Settings() {
		prefs = Gdx.app.getPreferences("TiltAndTumble");
	}

	public boolean isUseDpad() {
		return prefs.getBoolean(Setting.USE_DPAD.name(),
				!Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer));
	}

	public void setUseDpad(boolean useDpad) {
		prefs.putBoolean(Setting.USE_DPAD.name(), useDpad);
		notifyObservers(new SettingsUpdate(Setting.USE_DPAD, useDpad));
	}

	public boolean isDebugRender() {
		return prefs.getBoolean(Setting.DEBUG_RENDER.name(), false);
	}

	public void setDebugRender(boolean debugRender) {
		prefs.putBoolean(Setting.DEBUG_RENDER.name(), debugRender);
		notifyObservers(new SettingsUpdate(Setting.DEBUG_RENDER, debugRender));
	}

	public boolean isMusicOn() {
		return prefs.getBoolean(Setting.MUSIC.name(), true);
	}

	public void setMusic(boolean music) {
		prefs.putBoolean(Setting.MUSIC.name(), music);
		notifyObservers(new SettingsUpdate(Setting.MUSIC, music));
	}

	public boolean isSoundEffectOn() {
		return prefs.getBoolean(Setting.SOUND_EFFECTS.name(), true);
	}

	public void setSoundEffect(boolean soundEffect) {
		prefs.putBoolean(Setting.SOUND_EFFECTS.name(), soundEffect);
		notifyObservers(new SettingsUpdate(Setting.SOUND_EFFECTS, soundEffect));
	}

	public void save() {
		prefs.flush();
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
