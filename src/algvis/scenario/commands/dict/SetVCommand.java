package algvis.scenario.commands.dict;

import org.jdom.Element;

import algvis.core.Dictionary;
import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetVCommand implements Command {
	private final Dictionary T;
	private final Node newV, oldV;

	public SetVCommand(Dictionary T, Node newV) {
		this.T = T;
		oldV = T.getV();
		this.newV = newV;
	}

	@Override
	public void execute() {
		T.setV(newV);
	}

	@Override
	public void unexecute() {
		T.setV(oldV);
	}

	@Override
	public Element getXML() {
		Element e = new Element("setV");
		if (newV != null) {
			e.setAttribute("newVKey", Integer.toString(newV.key));
		} else {
			e.setAttribute("newV", "null");
		}
		if (oldV != null) {
			e.setAttribute("oldVKey", Integer.toString(oldV.key));
		} else {
			e.setAttribute("oldV", "null");
		}
		return e;
	}

}
