package algvis.scenario;

import org.jdom.Element;

import algvis.bst.BST;
import algvis.bst.BSTNode;

public class SetBSTNodeVCommand implements Command {
	private BST T;
	private BSTNode v;
	private boolean setted;
	private String name;

	public SetBSTNodeVCommand(BST T, BSTNode v, boolean setted) {
		this.T = T;
		this.v = v;
		this.setted = setted;
		if (setted) {
			name = "setNode";
		} else {
			name = "unsetNode";
		}
	}

	@Override
	public void execute() {
		if (setted) {
			T.setNodeV(v);
		} else {
			T.unsetNodeV();
		}
	}

	@Override
	public void unexecute() {
		if (setted) {
			T.unsetNodeV();
		} else {
			T.setNodeV(v);
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element(name);
		e.setAttribute("key", Integer.toString(v.key));
		return e;
	}

}
