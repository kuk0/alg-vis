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
package algvis.scenario.commands.skipnode;

import org.jdom.Element;

import algvis.scenario.commands.Command;
import algvis.skiplist.SkipNode;

public class SetRightCommand implements Command {
	private final SkipNode n, oldRight, newRight;

	public SetRightCommand(SkipNode n, SkipNode newRight) {
		this.n = n;
		oldRight = n.getRight();
		this.newRight = newRight;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setRight");
		e.setAttribute("key", Integer.toString(n.key));
		if (newRight != null) {
			e.setAttribute("newRight", Integer.toString(newRight.key));
		} else {
			e.setAttribute("newRight", "null");
		}
		if (oldRight != null) {
			e.setAttribute("oldRight", Integer.toString(oldRight.key));
		} else {
			e.setAttribute("oldRight", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setRight(newRight);
	}

	@Override
	public void unexecute() {
		n.setRight(oldRight);
	}

}
