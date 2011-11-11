package algvis.scenario;

import org.jdom.Element;

import algvis.bst.BSTNode;
import algvis.core.Node;

public class LinkLeftCommand extends Command {
	private BSTNode n1, n2;

	public LinkLeftCommand(BSTNode n1, BSTNode n2) {
		super(null);
		this.n1 = n1;
		this.n2 = n2;
	}

	@Override
	public void execute() {
		// see LinkRightCommand comments
		// n1.linkleft(n2);

		if (n2 != null && n2.state == Node.NOTLINKED)
			n2.state = Node.ALIVE;
	}

	@Override
	public void unexecute() {
		// see LinkRightCommand comments
		/*
		 * n1.left = null; if (n2 != null) { n2.parent = null; }
		 */

		if (n2 != null) {
			n2.state = Node.NOTLINKED;
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "linkLeft");
		e.setAttribute("parentKey", Integer.toString(n1.key));
		e.setAttribute("childKey", Integer.toString(n2.key));
		return e;
	}

}
