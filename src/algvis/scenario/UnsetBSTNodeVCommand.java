package algvis.scenario;

import org.jdom.Element;

import algvis.bst.BST;
import algvis.bst.BSTNode;

public class UnsetBSTNodeVCommand implements Command {
	private BST T;
	private BSTNode v;

	public UnsetBSTNodeVCommand(BST T, BSTNode v) {
		this.T = T;
		this.v = v;
	}

	@Override
	public void execute() {
		T.unsetNodeV();
	}

	@Override
	public void unexecute() {
		T.setNodeV(v);
	}

	@Override
	public Element getXML() {
		Element e = new Element("unsetNode");
		e.setAttribute("key", Integer.toString(v.key));
		return e;
	}

}
