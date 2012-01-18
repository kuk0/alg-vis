package algvis.scenario.commands.node;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class MoveCommand implements Command {
	private final int fromX, fromY, toX, toY;
	private final Node n;

	public MoveCommand(Node n, int toX, int toY) {
		this.n = n;
		fromX = n.tox;
		fromY = n.toy;
		this.toX = toX;
		this.toY = toY;
	}

	@Override
	public void execute() {
		n.goTo(toX, toY);
	}

	@Override
	public void unexecute() {
		n.goTo(fromX, fromY);
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
