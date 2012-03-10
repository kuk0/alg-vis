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

public class ArrowCommand implements Command {
	private final Node n, dir;
	private final int arrow;
	private final boolean drawArrow;
	private final String name;

	public ArrowCommand(Node n, boolean drawArrow) {
		this.n = n;
		dir = n.dir;
		arrow = n.arrow;
		this.drawArrow = drawArrow;
		if (drawArrow) {
			name = "arrow";
		} else {
			name = "noArrow";
		}
	}

	@Override
	public void execute() {
		if (drawArrow) {
			n.dir = dir;
			n.arrow = arrow;
		} else {
			n.noArrow();
		}
	}

	@Override
	public void unexecute() {
		if (drawArrow) {
			n.noArrow();
		} else {
			n.arrow = arrow;
			n.dir = dir;
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", name);
		e.setAttribute("key", Integer.toString(n.key));
		if (dir != null) {
			e.setAttribute("toNode", Integer.toString(dir.key));
		} else {
			e.setAttribute("angle", Integer.toString(arrow));
		}
		return e;
	}

}
