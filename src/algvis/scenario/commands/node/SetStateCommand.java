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

public class SetStateCommand implements Command {
	private final int fromState, toState;
	private final int fromX, fromY;
	private final Node n;

	public SetStateCommand(Node n, int toState) {
		this.n = n;
		this.toState = toState;
		fromState = n.state;
		fromX = n.tox;
		fromY = n.toy;
	}

	@Override
	public void execute() {
		n.setState(toState);
	}

	@Override
	public void unexecute() {
		if (toState == Node.LEFT || toState == Node.DOWN
				|| toState == Node.RIGHT) {
			n.goTo(fromX, fromY);
		}
		n.setState(fromState);
	}

	@Override
	public Element getXML() {
		Element e = new Element("node");
		e.setAttribute("action", "changeState");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("toState", Integer.toString(toState));
		e.setAttribute("fromState", Integer.toString(fromState));
		return e;
	}

}
