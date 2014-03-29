package edu.oswego.tiltandtumble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
    private boolean useDpad = false;
    private boolean debugRender = false;
    private boolean music = false;
    private boolean soundEffect = false;
    Preferences prefs;
    String dpadVal = "DPad Value";
    String debugVal = "Debug Value";
    String musicVal = "Music Value";
    String sEffVal = "Sound Effect Value";
    public Settings() {
    	prefs = Gdx.app.getPreferences("My Preferences");
    }
    public boolean isUseDpad() {
    	this.useDpad = prefs.getBoolean(dpadVal);
        return useDpad;
    }
    public void setUseDpad(boolean useDpad) {
    	prefs.putBoolean(dpadVal, useDpad);
        this.useDpad = useDpad;
    }
    public boolean isDebugRender() {
    this.debugRender = prefs.getBoolean(debugVal);
        return debugRender;
    }
    public void setDebugRender(boolean debugRender) {
    	prefs.putBoolean(debugVal, debugRender);
        this.debugRender = debugRender;
    }
    public boolean isMusicOn() {
    	this.music = prefs.getBoolean(musicVal);
        return music;
    }
    public void setMusic(boolean music) {
    	prefs.putBoolean(musicVal, music);
        this.music = music;
    }
    public boolean isSoundEffectOn() {
    	this.soundEffect = prefs.getBoolean(sEffVal);
        return soundEffect;
    }
    public void setSoundEffect(boolean soundEffect) {
    	prefs.putBoolean(sEffVal, soundEffect);
        this.soundEffect = soundEffect;
    }
	public void saveSettings() {
		prefs.flush();
	}
}
