package algvis.scenario.commands.bstnode;

import org.jdom.Element;

import algvis.bst.BSTNode;
import algvis.scenario.commands.Command;

public class SetParentCommand implements Command {
	private final BSTNode n, oldParent, newParent;

	public SetParentCommand(BSTNode n, BSTNode newParent) {
		this.n = n;
		oldParent = n.getParent();
		this.newParent = newParent;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setParent");
		e.setAttribute("key", Integer.toString(n.key));
		if (newParent != null) {
			e.setAttribute("newParent", Integer.toString(newParent.key));
		} else {
			e.setAttribute("newParent", "null");
		}
		if (oldParent != null) {
			e.setAttribute("oldParent", Integer.toString(oldParent.key));
		} else {
			e.setAttribute("oldParent", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setParent(newParent);
	}

	@Override
	public void unexecute() {
		n.setParent(oldParent);
	}

}
