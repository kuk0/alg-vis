package algvis.scenario.commands.node;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class MarkCommand implements Command {
	private Node n;
	private boolean marked;

	public MarkCommand(Node n, boolean marked) {
		this.n = n;
		this.marked = marked;
	}

	@Override
	public Element getXML() {
		Element e = new Element("mark");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("marked", Boolean.toString(marked));
		return e;
	}

	@Override
	public void execute() {
		if (marked) {
			n.mark();
		} else {
			n.unmark();
		}
	}

	@Override
	public void unexecute() {
		if (marked) {
			n.unmark();
		} else {
			n.mark();
		}
	}

}
