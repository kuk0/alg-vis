package algvis.scenario;

import org.jdom.Element;

import algvis.core.Node;

public class MoveCommand implements Command {
	private int fromX, fromY, toX, toY, steps;
	private Node n;

	public MoveCommand(Node n, int toX, int toY, int steps) {
		this.n = n;
		fromX = n.tox;
		fromY = n.toy;
		this.toX = toX;
		this.toY = toY;
		this.steps = steps;
	}

	@Override
	public void execute() {
		n.goToS(toX, toY, steps);
	}

	@Override
	public void unexecute() {
		n.goToS(fromX, fromY, steps);
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "move");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("posX", Integer.toString(toX));
		e.setAttribute("posY", Integer.toString(toY));
		e.setAttribute("fromPosX", Integer.toString(fromX));
		e.setAttribute("fromPosY", Integer.toString(fromY));
		return e;
	}

}
