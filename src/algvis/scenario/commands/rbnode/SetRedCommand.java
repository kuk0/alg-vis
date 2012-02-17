package algvis.scenario.commands.rbnode;

import org.jdom.Element;

import algvis.redblacktree.RBNode;
import algvis.scenario.commands.Command;

public class SetRedCommand implements Command {
	private final boolean oldRed, newRed;
	private final RBNode n;

	public SetRedCommand(RBNode n, boolean newRed) {
		this.n = n;
		oldRed = n.isRed();
		this.newRed = newRed;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setRed");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("wasRed", Boolean.toString(oldRed));
		e.setAttribute("isRed", Boolean.toString(newRed));
		return e;
	}

	@Override
	public void execute() {
		n.setRed(newRed);
	}

	@Override
	public void unexecute() {
		n.setRed(oldRed);
	}

}
