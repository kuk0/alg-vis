package algvis.scenario;

import org.jdom.Element;

public class PauseCommand implements Command {

	public PauseCommand() {
	}

	@Override
	public void execute() {
	}

	@Override
	public void unexecute() {
	}

	@Override
	public Element getXML() {
		Element e = new Element("pause");
		return e;
	}

}