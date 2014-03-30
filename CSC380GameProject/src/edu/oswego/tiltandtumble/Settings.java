package edu.oswego.tiltandtumble;

import java.util.ArrayList;
import java.util.List;

import edu.oswego.tiltandtumble.settings.SettingsObserver;
import edu.oswego.tiltandtumble.settings.SettingsUpdate;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
public class Settings {
	public static enum Setting {
		USE_DPAD,
		DEBUG_RENDER,
		MUSIC,
		SOUND_EFFECTS
	}
	Preferences prefs;
    private String dpadVal = "DPad Value";
    private String debugVal = "Debug Value";
    private String musicVal = "Music Value";
    private String sEffVal = "Sound Effect Value";
    
	 public Settings() {
	    	prefs = Gdx.app.getPreferences("My Preferences");
	    	this.useDpad = prefs.getBoolean(dpadVal);
	    	this.debugRender = prefs.getBoolean(debugVal);
	    	this.music = prefs.getBoolean(musicVal);
	    	this.soundEffect = prefs.getBoolean(sEffVal);
	    }

	private final List<SettingsObserver> observers = new ArrayList<SettingsObserver>();

	private boolean useDpad = false;
	private boolean debugRender = false;
	private boolean music = false;
	private boolean soundEffect = false;
	
	public boolean isUseDpad() {
		return useDpad;
	}

	public void setUseDpad(boolean useDpad) {
		prefs.putBoolean(dpadVal, useDpad);
		this.useDpad = useDpad;
		notifyObservers(new SettingsUpdate(Setting.USE_DPAD, useDpad));
	}

	public boolean isDebugRender() {
		return debugRender;
	}

	public void setDebugRender(boolean debugRender) {
		prefs.putBoolean(debugVal, debugRender);
		this.debugRender = debugRender;
		notifyObservers(new SettingsUpdate(Setting.DEBUG_RENDER, debugRender));
	}

	public boolean isMusicOn() {
		return music;
	}

	public void setMusic(boolean music) {
		prefs.putBoolean(musicVal, music);
		this.music = music;
		notifyObservers(new SettingsUpdate(Setting.MUSIC, music));
	}

	public boolean isSoundEffectOn() {
		return soundEffect;
	}

	public void setSoundEffect(boolean soundEffect) {
		prefs.putBoolean(sEffVal, soundEffect);
		this.soundEffect = soundEffect;
		notifyObservers(new SettingsUpdate(Setting.SOUND_EFFECTS, soundEffect));
	}
	public void saveSettings() {
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
