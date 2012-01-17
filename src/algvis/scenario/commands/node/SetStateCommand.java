package algvis.scenario.commands.node;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetStateCommand implements Command {
	private final int fromState, toState;
	private final int fromX, fromY;
	private final Node n;

	public SetStateCommand(Node n, int toState) {
		this.n = n;
		this.toState = toState;
		fromState = n.state;
		fromX = n.tox;
		fromY = n.toy;
	}

	@Override
	public void execute() {
		n.setState(toState);
	}

	@Override
	public void unexecute() {
		if (toState == Node.LEFT || toState == Node.DOWN
				|| toState == Node.RIGHT) {
			n.goTo(fromX, fromY);
		}
		n.setState(fromState);
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "changeState");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("toState", Integer.toString(toState));
		e.setAttribute("fromState", Integer.toString(fromState));
		return e;
	}

}
