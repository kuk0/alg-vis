package algvis.scenario.commands.splay3;

import org.jdom.Element;

import algvis.scenario.commands.Command;
import algvis.splaytree.SplayNode;
import algvis.splaytree.SplayTree;

public class SetVVCommand implements Command {
	private final SplayTree T;
	private final SplayNode newVV, oldVV;

	public SetVVCommand(SplayTree T, SplayNode newVV) {
		this.T = T;
		oldVV = T.getVV();
		this.newVV = newVV;
	}

	@Override
	public void execute() {
		T.setVV(newVV);
	}

	@Override
	public void unexecute() {
		T.setVV(oldVV);
	}

	@Override
	public Element getXML() {
		Element e = new Element("setVV");
		if (newVV != null) {
			e.setAttribute("newVVKey", Integer.toString(newVV.key));
		} else {
			e.setAttribute("newVV", "null");
		}
		if (oldVV != null) {
			e.setAttribute("oldVVKey", Integer.toString(oldVV.key));
		} else {
			e.setAttribute("oldVV", "null");
		}
		return e;
	}

}
