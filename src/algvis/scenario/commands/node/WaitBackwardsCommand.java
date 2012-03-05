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

public class WaitBackwardsCommand implements Command {
	private final Node n;

	public WaitBackwardsCommand(Node n) {
		this.n = n;
	}

	@Override
	public Element getXML() {
		Element e = new Element("waitBackwards");
		e.setAttribute("nodeKey", Integer.toString(n.key));
		return e;
	}

	@Override
	public void execute() {
	}

	@Override
	public void unexecute() {
		while (n.x != n.tox || n.y != n.toy) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				n.x = n.tox;
				n.y = n.toy;
				break;
			}
		}
	}

}
