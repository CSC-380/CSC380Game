package edu.oswego.tiltandtumble.worldObjects;

public interface TeleporterSelectorStrategy {
	public TeleporterTarget getNext();
	public void addTarget(TeleporterTarget target);
}
