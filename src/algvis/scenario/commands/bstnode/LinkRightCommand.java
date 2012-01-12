package algvis.scenario.commands.bstnode;

import org.jdom.Element;

import algvis.bst.BSTNode;
import algvis.scenario.commands.Command;

public class LinkRightCommand implements Command {
	private final BSTNode n1, n2;
	private final boolean linked;

	public LinkRightCommand(BSTNode n1, BSTNode n2, boolean linked) {
		this.n1 = n1;
		this.n2 = n2;
		this.linked = linked;
	}

	@Override
	public void execute() {
		if (linked) {
			n1.linkRight(n2);
		} else if (n1.right != null) {
			n1.unlinkRight();
		}
	}

	@Override
	public void unexecute() {
		if (!linked) {
			n1.linkRight(n2);
		} else if (n1.right != null) {
			n1.unlinkRight();
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "linkRight");
		e.setAttribute("linked", Boolean.toString(linked));
		e.setAttribute("parentKey", Integer.toString(n1.key));
		if (n2 != null) {
			e.setAttribute("childKey", Integer.toString(n2.key));
		} else {
			e.setAttribute("childKey", "null");
		}
		return e;
	}

}
