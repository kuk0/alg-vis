package algvis.scenario.commands.node;

import java.awt.Color;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetFgColorCommand implements Command {
	private final Color oldFgColor, newFgColor;
	private final Node n;

	public SetFgColorCommand(Node n, Color newfgColor) {
		this.n = n;
		oldFgColor = n.getFgColor();
		this.newFgColor = newfgColor;
	}

	@Override
	public void execute() {
		n.fgColor(newFgColor);
	}

	@Override
	public void unexecute() {
		n.fgColor(oldFgColor);
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "changeColor");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("fgColor", newFgColor.toString());
		return e;
	}

}
