package algvis.scenario;

import org.jdom.Element;

public class PauseCommand extends Command {

	public PauseCommand() {
		super(null);
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
