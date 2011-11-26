package algvis.scenario;

import org.jdom.Element;

import algvis.bst.BSTNode;

public class UnsetBSTParentCommand implements Command {
	private BSTNode n, parent;

	public UnsetBSTParentCommand(BSTNode n, BSTNode parent) {
		this.n = n;
		this.parent = parent;
	}

	@Override
	public void execute() {
		n.unsetParent();
	}

	@Override
	public void unexecute() {
		n.parent = parent;
	}

	@Override
	public Element getXML() {
		Element e = new Element("unsetParent");
		e.setAttribute("key", Integer.toString(n.key));
		if (parent != null) {
			e.setAttribute("parentKey", Integer.toString(parent.key));
		} else {
			e.setAttribute("parent", "null");
		}
		return e;
	}

}
