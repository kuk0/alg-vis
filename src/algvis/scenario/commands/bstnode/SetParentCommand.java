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
package algvis.scenario.commands.bstnode;

import org.jdom.Element;

import algvis.bst.BSTNode;
import algvis.scenario.commands.Command;

public class SetParentCommand implements Command {
	private final BSTNode n, oldParent, newParent;

	public SetParentCommand(BSTNode n, BSTNode newParent) {
		this.n = n;
		oldParent = n.getParent();
		this.newParent = newParent;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setParent");
		e.setAttribute("key", Integer.toString(n.key));
		if (newParent != null) {
			e.setAttribute("newParent", Integer.toString(newParent.key));
		} else {
			e.setAttribute("newParent", "null");
		}
		if (oldParent != null) {
			e.setAttribute("oldParent", Integer.toString(oldParent.key));
		} else {
			e.setAttribute("oldParent", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setParent(newParent);
	}

	@Override
	public void unexecute() {
		n.setParent(oldParent);
	}

}
