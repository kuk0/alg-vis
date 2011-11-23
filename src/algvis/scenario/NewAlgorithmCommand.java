package algvis.scenario;

import org.jdom.Element;

public class NewAlgorithmCommand implements Command {
	private String name;

	public NewAlgorithmCommand(String name) {
		this.name = name;
	}

	@Override
	public void execute() {
		/*
		 * these lines will contain code needed when adding into scenario (in
		 * the middle of scenario, between 2 algorithms of scenario,...) is
		 * enabled
		 */
		// tell Buttons to disable Insert,...
	}

	@Override
	public void unexecute() {
		// tell Buttons to enable Insert,...
	}

	public String getName() {
		return name;
	}

	@Override
	public Element getXML() {
		Element e = new Element(name);
		return e;
	}

}
