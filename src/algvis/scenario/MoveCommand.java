package algvis.scenario;

import org.jdom.Element;

import algvis.core.Node;

public class MoveCommand extends Command {
	int fromX, fromY, toX, toY;

	public MoveCommand(Node n, int toX, int toY) {
		super(n);
		fromX = n.x;
		fromY = n.y;
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
		return e;
	}

}
