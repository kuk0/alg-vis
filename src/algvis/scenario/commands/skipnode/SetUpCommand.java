package algvis.scenario.commands.skipnode;

import org.jdom.Element;

import algvis.scenario.commands.Command;
import algvis.skiplist.SkipNode;

public class SetUpCommand implements Command {
	private final SkipNode n, oldUp, newUp;

	public SetUpCommand(SkipNode n, SkipNode newUp) {
		this.n = n;
		oldUp = n.getUp();
		this.newUp = newUp;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setUp");
		e.setAttribute("key", Integer.toString(n.key));
		if (newUp != null) {
			e.setAttribute("newUp", Integer.toString(newUp.key));
		} else {
			e.setAttribute("newUp", "null");
		}
		if (oldUp != null) {
			e.setAttribute("oldUp", Integer.toString(oldUp.key));
		} else {
			e.setAttribute("oldUp", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setUp(newUp);
	}

	@Override
	public void unexecute() {
		n.setUp(oldUp);
	}

}
