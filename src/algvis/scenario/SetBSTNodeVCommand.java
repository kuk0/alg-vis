package algvis.scenario;

import org.jdom.Element;

import algvis.bst.BST;
import algvis.bst.BSTNode;

public class SetBSTNodeVCommand implements Command {
	private BST T;
	private BSTNode v, old;

	public SetBSTNodeVCommand(BST T, BSTNode v, BSTNode old) {
		this.T = T;
		this.v = v;
		this.old = old;
	}

	@Override
	public void execute() {
		T.setNodeV(v);
	}

	@Override
	public void unexecute() {
		T.setNodeV(old);
	}

	@Override
	public Element getXML() {
		Element e = new Element("setNode");
		if (v != null) {
			e.setAttribute("vKey", Integer.toString(v.key));
		} else {
			e.setAttribute("v", "null");
		}
		if (old != null) {
			e.setAttribute("oldKey", Integer.toString(old.key));
		} else {
			e.setAttribute("v", "null");
		}
		return e;
	}

}
