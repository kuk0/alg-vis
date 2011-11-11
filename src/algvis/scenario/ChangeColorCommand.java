package algvis.scenario;

import java.awt.Color;

import org.jdom.Element;

import algvis.core.Node;

public class ChangeColorCommand extends Command {
	Color fromBgColor, toBgColor;
	
	public ChangeColorCommand(Node n, Color toBgColor) {
		super(n);
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
