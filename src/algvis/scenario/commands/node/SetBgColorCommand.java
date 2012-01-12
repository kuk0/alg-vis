package algvis.scenario.commands.node;

import java.awt.Color;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetBgColorCommand implements Command {
	private final Color fromBgColor, toBgColor;
	private final Node n;

	public SetBgColorCommand(Node n, Color toBgColor) {
		this.n = n;
		fromBgColor = n.bgcolor;
		this.toBgColor = toBgColor;
	}

	@Override
	public void execute() {
		n.bgColor(toBgColor);
	}

	@Override
	public void unexecute() {
		n.bgColor(fromBgColor);
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "changeColor");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("bgColor", toBgColor.toString());
		return e;
	}

}
