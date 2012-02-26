package algvis.scenario.commands.dict;

import org.jdom.Element;

import algvis.core.Dictionary;
import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetRootCommand implements Command {
	private final Dictionary T;
	private final Node newRoot, oldRoot;

	public SetRootCommand(Dictionary T, Node newRoot) {
		this.T = T;
		oldRoot = T.getRoot();
		this.newRoot = newRoot;
	}

	@Override
	public void execute() {
		T.setRoot(newRoot);
	}

	@Override
	public void unexecute() {
		T.setRoot(oldRoot);
	}

	@Override
	public Element getXML() {
		Element e = new Element("setRoot");
		if (newRoot != null) {
			e.setAttribute("newRootKey", Integer.toString(newRoot.key));
		} else {
			e.setAttribute("newRoot", "null");
		}
		if (oldRoot != null) {
			e.setAttribute("oldRootKey", Integer.toString(oldRoot.key));
		} else {
			e.setAttribute("oldRoot", "null");
		}
		return e;
	}

}
