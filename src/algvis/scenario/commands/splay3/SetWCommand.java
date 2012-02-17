package algvis.scenario.commands.splay3;

import org.jdom.Element;

import algvis.scenario.commands.Command;
import algvis.splaytree.SplayNode;
import algvis.splaytree.SplayTree;

public class SetWCommand implements Command {
	private final SplayTree T;
	private final SplayNode oldW, newW;
	private final int order;

	public SetWCommand(SplayTree T, SplayNode newW, int order) {
		this.T = T;
		this.order = order;
		switch (order) {
		case 1:
			oldW = T.getW1();
			break;
		case 2:
			oldW = T.getW2();
			break;
		default:
			oldW = null;
			System.err
					.println("SetWCommand bad \"order\" argument (must be 1 or 2)");
		}
		this.newW = newW;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setW" + order);
		if (newW != null) {
			e.setAttribute("newWKey", Integer.toString(newW.key));
		} else {
			e.setAttribute("newW", "null");
		}
		if (oldW != null) {
			e.setAttribute("oldWKey", Integer.toString(oldW.key));
		} else {
			e.setAttribute("oldW", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		if (order == 1) {
			T.setW1(newW);
		} else {
			T.setW2(newW);
		}
	}

	@Override
	public void unexecute() {
		if (order == 1) {
			T.setW1(oldW);
		} else {
			T.setW2(oldW);
		}
	}

}
