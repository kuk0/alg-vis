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

public class MoveCommand implements Command {
	private final int fromX, fromY, toX, toY;
	private final Node n;

	public MoveCommand(Node n, int toX, int toY) {
		this.n = n;
		fromX = n.tox;
		fromY = n.toy;
		this.toX = toX;
		this.toY = toY;
	}

	@Override
	public void execute() {
		n.goTo(toX, toY);
	}

	@Override
	public void unexecute() {
		n.goTo(fromX, fromY);
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "move");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("posX", Integer.toString(toX));
		e.setAttribute("posY", Integer.toString(toY));
		e.setAttribute("fromPosX", Integer.toString(fromX));
		e.setAttribute("fromPosY", Integer.toString(fromY));
		return e;
	}

}
