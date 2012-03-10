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

public class SetLeftCommand implements Command {
	private final BSTNode n, oldLeft, newLeft;

	public SetLeftCommand(BSTNode n, BSTNode newLeft) {
		this.n = n;
		oldLeft = n.getLeft();
		this.newLeft = newLeft;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setLeft");
		e.setAttribute("key", Integer.toString(n.key));
		if (newLeft != null) {
			e.setAttribute("newLeft", Integer.toString(newLeft.key));
		} else {
			e.setAttribute("newLeft", "null");
		}
		if (oldLeft != null) {
			e.setAttribute("oldLeft", Integer.toString(oldLeft.key));
		} else {
			e.setAttribute("oldLeft", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setLeft(newLeft);
	}

	@Override
	public void unexecute() {
		n.setLeft(oldLeft);
	}

}
