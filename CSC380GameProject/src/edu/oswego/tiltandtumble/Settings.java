package edu.oswego.tiltandtumble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
    private boolean useDpad = false;
    private boolean debugRender = false;
    private boolean music = false;
    private boolean soundEffect = false;
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
    public boolean isUseDpad() {
        return useDpad;
    }
    public void setUseDpad(boolean useDpad) {
    	prefs.putBoolean(dpadVal, useDpad);
        this.useDpad = useDpad;
    }
    public boolean isDebugRender() {
        return debugRender;
    }
    public void setDebugRender(boolean debugRender) {
    	prefs.putBoolean(debugVal, debugRender);
        this.debugRender = debugRender;
    }
    public boolean isMusicOn() {
        return music;
    }
    public void setMusic(boolean music) {
    	prefs.putBoolean(musicVal, music);
        this.music = music;
    }
    public boolean isSoundEffectOn() {
    	
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
