package algvis.scenario.commands.bstnode;

import org.jdom.Element;

import algvis.bst.BSTNode;
import algvis.scenario.commands.Command;

public class SetLevelCommand implements Command {
	private final BSTNode n;
	private final int fromLevel, toLevel;

	public SetLevelCommand(BSTNode n, int toLevel) {
		this.n = n;
		this.fromLevel = n.getLevel();
		this.toLevel = toLevel;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setLevel");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("fromLevel", Integer.toString(fromLevel));
		e.setAttribute("toLevel", Integer.toString(toLevel));
		return e;
	}

	@Override
	public void execute() {
		n.setLevel(toLevel);
	}

	@Override
	public void unexecute() {
		n.setLevel(fromLevel);
	}

}
