package algvis.scenario.commands.skipnode;

import org.jdom.Element;

import algvis.scenario.commands.Command;
import algvis.skiplist.SkipNode;

public class SetLeftCommand implements Command {
	private final SkipNode n, oldLeft, newLeft;

	public SetLeftCommand(SkipNode n, SkipNode newLeft) {
		this.n = n;
		oldLeft = n.getLeft();
		this.newLeft = newLeft;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setLeft");
		e.setAttribute("key", Integer.toString(n.key));
		if (newLeft != null) {
			e.setAttribute("newLeft", Integer.toString(newLeft.key));
		} else {
			e.setAttribute("newLeft", "null");
		}
		if (oldLeft != null) {
			e.setAttribute("oldLeft", Integer.toString(oldLeft.key));
		} else {
			e.setAttribute("oldLeft", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setLeft(newLeft);
	}

	@Override
	public void unexecute() {
		n.setLeft(oldLeft);
	}

}
