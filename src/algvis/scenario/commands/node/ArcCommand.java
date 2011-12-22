package algvis.scenario.commands.node;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class ArcCommand implements Command {
	private Node fromNode, toNode;
	private boolean setted;

	public ArcCommand(Node fromNode, Node toNode, boolean setted) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.setted = setted;
	}

	@Override
	public Element getXML() {
		Element e = new Element("Arc");
		e.setAttribute("fromNode", Integer.toString(fromNode.key));
		e.setAttribute("toNode", Integer.toString(toNode.key));
		e.setAttribute("setted", Boolean.toString(setted));
		return e;
	}

	@Override
	public void execute() {
		if (setted) {
			fromNode.setArc(toNode);
		} else {
			fromNode.noArc();
		}
	}

	@Override
	public void unexecute() {
		if (setted) {
			fromNode.noArc();
		} else {
			fromNode.setArc(toNode);
		}
	}

}
