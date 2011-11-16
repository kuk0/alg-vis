package algvis.scenario;

import org.jdom.Element;

import algvis.core.Node;

public class ArrowCommand implements Command {
	private Node n, dir;
	private int arrow;

	public ArrowCommand(Node n) {
		this.n = n;
		dir = n.dir;
		arrow = n.arrow;
	}

	@Override
	public void execute() {
		n.dir = dir;
		n.arrow = arrow;
	}

	@Override
	public void unexecute() {
		n.dir = null;
		n.arrow = Node.NOARROW;
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "arrow");
		if (dir != null) {
			e.setAttribute("toNode", Integer.toString(dir.key));
		} else {
			e.setAttribute("angle", Integer.toString(arrow));
		}
		return e;
	}

}
