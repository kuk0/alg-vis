package algvis.scenario.commands.skipnode;

import org.jdom.Element;

import algvis.scenario.commands.Command;
import algvis.skiplist.SkipNode;

public class SetRightCommand implements Command {
	private final SkipNode n, oldRight, newRight;

	public SetRightCommand(SkipNode n, SkipNode newRight) {
		this.n = n;
		oldRight = n.getRight();
		this.newRight = newRight;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setRight");
		e.setAttribute("key", Integer.toString(n.key));
		if (newRight != null) {
			e.setAttribute("newRight", Integer.toString(newRight.key));
		} else {
			e.setAttribute("newRight", "null");
		}
		if (oldRight != null) {
			e.setAttribute("oldRight", Integer.toString(oldRight.key));
		} else {
			e.setAttribute("oldRight", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setRight(newRight);
	}

	@Override
	public void unexecute() {
		n.setRight(oldRight);
	}

}
