package algvis.scenario.commands.node;

import java.awt.geom.Point2D;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetStateCommand implements Command {
	private int fromState, toState;
	private int fromX, fromY;
	private Node n;

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
		if (fromState == Node.UP) {
			Point2D p = n.D.M.screen.V.r2v(0, 0);
			n.goTo(n.tox, (int) p.getY() - 5 * n.D.radius);
			wait4Node();
		}
		n.setState(fromState);
	}

	private void wait4Node() {
		while (n.x != n.tox || n.y != n.toy) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				n.x = n.tox;
				n.y = n.toy;
				break;
			}
		}
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
