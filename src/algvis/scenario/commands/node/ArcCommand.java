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

import org.jdom.Element;

import algvis.core.Node;
import algvis.scenario.commands.Command;

public class ArcCommand implements Command {
	private final Node fromNode, toNode;
	private final boolean setted;

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
