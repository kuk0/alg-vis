package algvis.scenario.commands.gbnode;

import org.jdom.Element;

import algvis.scapegoattree.GBTree;
import algvis.scenario.commands.Command;

public class SetDelCommand implements Command {
	private final GBTree T;
	private final int oldDel, newDel;

	public SetDelCommand(GBTree T, int newDel) {
		this.T = T;
		oldDel = T.getDel();
		this.newDel = newDel;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setDel");
		e.setAttribute("GBTree", Integer.toString(T.hashCode()));
		e.setAttribute("oldDel", Integer.toString(oldDel));
		e.setAttribute("newDel", Integer.toString(newDel));
		return e;
	}

	@Override
	public void execute() {
		T.setDel(newDel);
	}

	@Override
	public void unexecute() {
		T.setDel(oldDel);
	}

}
