package edu.oswego.tiltandtumble.settings;

public interface SettingsObserver {
	public void handleSettingsChangeUpdate(SettingsUpdate update);
}
