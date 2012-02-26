package algvis.scenario.commands;

import org.jdom.Element;

import algvis.core.Buttons;

public class SetStatsCommand implements Command {
	private final Buttons B;
	private final String oldStats, newStats;

	public SetStatsCommand(Buttons B, String oldStats, String newStats) {
		this.B = B;
		this.oldStats = oldStats;
		this.newStats = newStats;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setStats");
		e.setAttribute("oldStats", oldStats);
		e.setAttribute("newStats", newStats);
		return e;
	}

	@Override
	public void execute() {
		B.setStats(newStats);
	}

	@Override
	public void unexecute() {
		B.setStats(oldStats);
	}

}
