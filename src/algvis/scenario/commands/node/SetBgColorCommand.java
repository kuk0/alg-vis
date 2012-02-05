package algvis.scenario.commands.node;

import java.awt.Color;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetBgColorCommand implements Command {
	private final Color oldBgColor, newBgColor;
	private final Node n;

	public SetBgColorCommand(Node n, Color newBgColor) {
		this.n = n;
		oldBgColor = n.getBgColor();
		this.newBgColor = newBgColor;
	}

	@Override
	public void execute() {
		n.bgColor(newBgColor);
	}

	@Override
	public void unexecute() {
		n.bgColor(oldBgColor);
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "changeColor");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("bgColor", newBgColor.toString());
		return e;
	}

}
