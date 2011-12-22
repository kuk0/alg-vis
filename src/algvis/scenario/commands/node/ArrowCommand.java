package algvis.scenario.commands.node;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class ArrowCommand implements Command {
	private Node n, dir;
	private int arrow;
	private boolean drawArrow;
	private String name;

	public ArrowCommand(Node n, boolean drawArrow) {
		this.n = n;
		dir = n.dir;
		arrow = n.arrow;
		this.drawArrow = drawArrow;
		if (drawArrow) {
			name = "arrow";
		} else {
			name = "noArrow";
		}
	}

	@Override
	public void execute() {
		if (drawArrow) {
			n.dir = dir;
			n.arrow = arrow;
		} else {
			n.noArrow();
		}
	}

	@Override
	public void unexecute() {
		if (drawArrow) {
			n.noArrow();
		} else {
			n.arrow = arrow;
			n.dir = dir;
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", name);
		e.setAttribute("key", Integer.toString(n.key));
		if (dir != null) {
			e.setAttribute("toNode", Integer.toString(dir.key));
		} else {
			e.setAttribute("angle", Integer.toString(arrow));
		}
		return e;
	}

}
