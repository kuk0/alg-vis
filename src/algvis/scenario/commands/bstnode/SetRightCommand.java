package algvis.scenario.commands.bstnode;

import org.jdom.Element;

import algvis.bst.BSTNode;
import algvis.scenario.commands.Command;

public class SetRightCommand implements Command {
	private final BSTNode n, oldRight, newRight;

	public SetRightCommand(BSTNode n, BSTNode newRight) {
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
