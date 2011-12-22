package algvis.scenario.commands.node;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetStateCommand implements Command {
	private int from, to;
	private Node n;

	public SetStateCommand(Node n, int to) {
		this.n = n;
		this.from = n.state;
		this.to = to;
	}

	@Override
	public void execute() {
		if (to == Node.OUT) {
			wait4Node();
		}
		n.setState(to);
	}

	@Override
	public void unexecute() {
		if (from == Node.UP) {
			wait4Node();
		}
		n.setState(from);
	}

	public void wait4Node() {
		while (n.x != n.tox || n.y != n.toy) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "changeState");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("state", Integer.toString(to));
		e.setAttribute("fromState", Integer.toString(from));
		return e;
	}

}
