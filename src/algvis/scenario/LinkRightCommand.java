package algvis.scenario;

import org.jdom.Element;

import algvis.bst.BSTNode;
import algvis.core.Node;

public class LinkRightCommand extends Command {
	private BSTNode n1, n2;
	
	public LinkRightCommand(BSTNode n1, BSTNode n2) {
		super(null);
		this.n1 = n1;
		this.n2 = n2;
	}
	
	@Override
	public void execute() {
		// see comment in unexecute()
		// n1.linkright(n2);
		
		if (n2 != null && n2.state == Node.NOTLINKED) n2.state = Node.ALIVE;
		/* actually, this hack don't work in one case:
		 * When you traverse the history backwards and you call ChangeStateCommand.unexecute(),
		 * the n2.state changes to other than NOTLINKED state. Consequently, if you click on "next" 
		 * button, this node will be drown with edge with his parent.
		 */
	}

	@Override
	public void unexecute() {
		// this don't work, because n2 wouldn't be linked with his parent and (because of this) drown
		/*
		 * n1.right = null; if (n2 != null) { n2.parent = null; }
		 */

		// and the hack is coming...
		if (n2 != null) {
			n2.state = Node.NOTLINKED;
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "linkRight");
		e.setAttribute("parentKey", Integer.toString(n1.key));
		e.setAttribute("childKey", Integer.toString(n2.key));
		return e;
	}

}
