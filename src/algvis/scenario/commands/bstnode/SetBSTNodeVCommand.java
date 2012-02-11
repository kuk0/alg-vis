package algvis.scenario.commands.bstnode;

import org.jdom.Element;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.scenario.commands.Command;

public class SetBSTNodeVCommand implements Command {
	private final BST T;
	private final BSTNode newV, oldV;

	public SetBSTNodeVCommand(BST T, BSTNode newV, BSTNode oldV) {
		this.T = T;
		this.newV = newV;
		this.oldV = oldV;
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
		Element e = new Element("setNodeV");
		if (newV != null) {
			e.setAttribute("newKey", Integer.toString(newV.key));
		} else {
			e.setAttribute("newV", "null");
		}
		if (oldV != null) {
			e.setAttribute("oldKey", Integer.toString(oldV.key));
		} else {
			e.setAttribute("oldV", "null");
		}
		return e;
	}

}
