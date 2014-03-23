package edu.oswego.tiltandtumble.worldObjects;

import java.util.ArrayList;
import java.util.List;

public class TeleporterRoundRobinSelector implements TeleporterSelectorStrategy {
	private int next;
	private final List<TeleporterTarget> targets;

	public TeleporterRoundRobinSelector() {
		this.targets = new ArrayList<TeleporterTarget>(5);
		this.next = 0;
	}

	@Override
	public TeleporterTarget getNext() {
		TeleporterTarget t = targets.get(next);
		next = (next + 1) % targets.size();
		return t;
	}

	@Override
	public void addTarget(TeleporterTarget target) {
		targets.add(target);
	}
}