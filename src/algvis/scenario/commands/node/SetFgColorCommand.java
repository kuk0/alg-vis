/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
