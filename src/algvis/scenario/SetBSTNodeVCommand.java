package algvis.scenario;

import org.jdom.Element;

import algvis.bst.BST;
import algvis.bst.BSTNode;

public class SetBSTNodeVCommand implements Command {
	private BST T;
	private BSTNode v;

	public SetBSTNodeVCommand(BST T, BSTNode v) {
		this.T = T;
		this.v = v;
	}

	@Override
	public void execute() {
		T.setNodeV(v);
	}

	@Override
	public void unexecute() {
		T.unsetNodeV();
	}

	@Override
	public Element getXML() {
		Element e = new Element("setNode");
		e.setAttribute("key", Integer.toString(v.key));
		return e;
	}

}
