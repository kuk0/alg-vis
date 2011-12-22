package algvis.scenario.commands.node;

import java.awt.Color;

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetFgColorCommand implements Command {
	private Color fromfgColor, tofgColor;
	private Node n;

	public SetFgColorCommand(Node n, Color tofgColor) {
		this.n = n;
		fromfgColor = n.fgcolor;
		this.tofgColor = tofgColor;
	}

	@Override
	public void execute() {
		n.fgColor(tofgColor);
	}

	@Override
	public void unexecute() {
		n.fgColor(fromfgColor);
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "changeColor");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("fgColor", tofgColor.toString());
		return e;
	}

}
