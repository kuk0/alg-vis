package algvis.scenario.commands.skipnode;

import org.jdom.Element;

import algvis.scenario.commands.Command;
import algvis.skiplist.SkipNode;

public class SetDownCommand implements Command {
	private final SkipNode n, oldDown, newDown;

	public SetDownCommand(SkipNode n, SkipNode newDown) {
		this.n = n;
		oldDown = n.getDown();
		this.newDown = newDown;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setDown");
		e.setAttribute("key", Integer.toString(n.key));
		if (newDown != null) {
			e.setAttribute("newDown", Integer.toString(newDown.key));
		} else {
			e.setAttribute("newDown", "null");
		}
		if (oldDown != null) {
			e.setAttribute("oldDown", Integer.toString(oldDown.key));
		} else {
			e.setAttribute("oldDown", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setDown(newDown);
	}

	@Override
	public void unexecute() {
		n.setDown(oldDown);
	}

}
