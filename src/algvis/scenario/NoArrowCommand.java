package algvis.scenario;

import org.jdom.Element;

import algvis.core.Node;

public class NoArrowCommand implements Command {
	private Node n, dir;
	private int arrow;

	public NoArrowCommand(Node n) {
		this.n = n;
		dir = n.dir;
		arrow = n.arrow;
	}

	@Override
	public void execute() {
		n.noArrow();
	}

	@Override
	public void unexecute() {
		n.arrow = arrow;
		n.dir = dir;
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "noArrow");
		return e;
	}

}
