package algvis.scenario.commands.gbnode;

import org.jdom.Element;

import algvis.scapegoattree.GBNode;
import algvis.scenario.commands.Command;

public class SetDeletedCommand implements Command {
	private final GBNode n;
	private final boolean oldDeleted, newDeleted;

	public SetDeletedCommand(GBNode n, boolean newDeleted) {
		this.n = n;
		oldDeleted = n.isDeleted();
		this.newDeleted = newDeleted;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setDeleted");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("wasDeleted", Boolean.toString(oldDeleted));
		e.setAttribute("isDeleted", Boolean.toString(newDeleted));
		return e;
	}

	@Override
	public void execute() {
		n.setDeleted(newDeleted);
	}

	@Override
	public void unexecute() {
		n.setDeleted(oldDeleted);
	}

}
