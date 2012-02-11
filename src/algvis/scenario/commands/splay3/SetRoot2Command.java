package algvis.scenario.commands.splay3;

import org.jdom.Element;

import algvis.scenario.commands.Command;
import algvis.splaytree.SplayNode;
import algvis.splaytree.SplayTree;

public class SetRoot2Command implements Command {
	private final SplayTree T;
	private final SplayNode oldRoot2, newRoot2;

	public SetRoot2Command(SplayTree T, SplayNode newRoot2) {
		this.T = T;
		oldRoot2 = T.getRoot2();
		this.newRoot2 = newRoot2;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setRoot2");
		if (newRoot2 != null) {
			e.setAttribute("newRoot2Key", Integer.toString(newRoot2.key));
		} else {
			e.setAttribute("newRoot2", "null");
		}
		if (oldRoot2 != null) {
			e.setAttribute("oldRoot2Key", Integer.toString(oldRoot2.key));
		} else {
			e.setAttribute("oldRoot2", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		T.setRoot2(newRoot2);
	}

	@Override
	public void unexecute() {
		T.setRoot2(oldRoot2);
	}

}
