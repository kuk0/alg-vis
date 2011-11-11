package algvis.scenario;

import org.jdom.Element;

import algvis.core.Node;

public class ChangeStateCommand extends Command {
	private int from, to;

	public ChangeStateCommand(Node n, int to) {
		super(n);
		this.n = n;
		this.from = n.state;
		this.to = to;
	}

	@Override
	public void execute() {
		n.setState(to);
	}

	@Override
	public void unexecute() {
		n.setState(from);
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "changeState");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("state", Integer.toString(to));
		return e;
	}

}
