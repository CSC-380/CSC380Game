package edu.oswego.tiltandtumble;
public class Settings {
    private boolean useDpad;
    private boolean debugRender = false;
    private boolean music = false;
    private boolean soundEffect = false;
    
    public boolean isUseDpad() {
        return useDpad;
    }
    public void setUseDpad(boolean useDpad) {
        this.useDpad = useDpad;
    }
    public boolean isDebugRender() {
        return debugRender;
    }
    public void setDebugRender(boolean debugRender) {
        this.debugRender = debugRender;
    }
    public boolean isMusicOn() {
        return music;
    }
    public void setMusic(boolean music) {
        this.music = music;
    }
    public boolean isSoundEffectOn() {
        return soundEffect;
    }
    public void setSoundEffect(boolean soundEffect) {
        this.soundEffect = soundEffect;
    }
}
