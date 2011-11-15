package algvis.scenario;

import java.awt.Color;

import org.jdom.Element;

import algvis.core.Node;

public class ChangeColorCommand implements Command {
	private Color fromBgColor, toBgColor;
	private Node n;

	public ChangeColorCommand(Node n, Color toBgColor) {
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
