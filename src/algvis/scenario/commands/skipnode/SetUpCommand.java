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

public class SetUpCommand implements Command {
	private final SkipNode n, oldUp, newUp;

	public SetUpCommand(SkipNode n, SkipNode newUp) {
		this.n = n;
		oldUp = n.getUp();
		this.newUp = newUp;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setUp");
		e.setAttribute("key", Integer.toString(n.key));
		if (newUp != null) {
			e.setAttribute("newUp", Integer.toString(newUp.key));
		} else {
			e.setAttribute("newUp", "null");
		}
		if (oldUp != null) {
			e.setAttribute("oldUp", Integer.toString(oldUp.key));
		} else {
			e.setAttribute("oldUp", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setUp(newUp);
	}

	@Override
	public void unexecute() {
		n.setUp(oldUp);
	}

}
