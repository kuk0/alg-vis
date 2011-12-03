package algvis.scenario;

import org.jdom.Element;

import algvis.core.Buttons;

public class EnableButtonsCommand implements Command {
	private Buttons b;
	private boolean enabled;

	public EnableButtonsCommand(Buttons b, boolean enabled) {
		this.b = b;
		this.enabled = enabled;
	}

	@Override
	public void execute() {
		if (enabled) {
			b.enableAll();
		} else {
			b.disableAll();
		}
	}

	@Override
	public void unexecute() {
		if (enabled) {
			b.disableAll();
		} else {
			b.enableAll();
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element("enableButtons");
		e.setAttribute("enabled", Boolean.toString(enabled));
		return e;
	}

}
