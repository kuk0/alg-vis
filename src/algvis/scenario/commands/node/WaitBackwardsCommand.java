package algvis.scenario.commands.node;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class WaitBackwardsCommand implements Command {
	private final Node n;

	public WaitBackwardsCommand(Node n) {
		this.n = n;
	}

	@Override
	public Element getXML() {
		Element e = new Element("waitBackwards");
		e.setAttribute("nodeKey", Integer.toString(n.key));
		return e;
	}

	@Override
	public void execute() {
	}

	@Override
	public void unexecute() {
		while (n.x != n.tox || n.y != n.toy) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				n.x = n.tox;
				n.y = n.toy;
				break;
			}
		}
	}

}
