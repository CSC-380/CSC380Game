package edu.oswego.tiltandtumble.worldObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeleporterRandomSelector implements TeleporterSelectorStrategy {
	private final Random rand = new Random();
	private final List<TeleporterTarget> targets;

	public TeleporterRandomSelector() {
		this.targets = new ArrayList<TeleporterTarget>(5);
	}

	@Override
	public TeleporterTarget getNext() {
		return targets.get(rand.nextInt(targets.size()));
	}

	@Override
	public void addTarget(TeleporterTarget target) {
		targets.add(target);
	}
}
