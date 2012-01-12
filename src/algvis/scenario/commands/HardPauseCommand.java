package algvis.scenario.commands;

import org.jdom.Element;

import algvis.core.DataStructure;

public class HardPauseCommand implements Command {
	private final DataStructure d;
	/** if false then it's start of an algorithm else it's end of an algorithm */
	private final boolean enabled;

	public HardPauseCommand(DataStructure d, boolean enabled) {
		this.d = d;
		this.enabled = enabled;
	}

	@Override
	public void execute() {
		if (enabled) {
			d.M.B.enableAll();
			d.scenario.stop();
		} else {
			d.M.B.disableAll();
		}
	}

	@Override
	public void unexecute() {
		if (enabled) {
			d.M.B.disableAll();
		} else {
			d.M.B.enableAll();
			d.scenario.stop();
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element("enableButtons");
		e.setAttribute("enabled", Boolean.toString(enabled));
		return e;
	}

}
