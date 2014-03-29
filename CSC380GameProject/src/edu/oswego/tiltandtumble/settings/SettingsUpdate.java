package edu.oswego.tiltandtumble.settings;

import edu.oswego.tiltandtumble.Settings.Setting;

public class SettingsUpdate {
	private final Setting name;
	private final boolean value;

	public SettingsUpdate(Setting name, boolean value) {
		this.name = name;
		this.value = value;
	}

	public Setting getSetting() {
		return name;
	}

	public boolean getValue() {
		return value;
	}
}
