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

public class SetDownCommand implements Command {
	private final SkipNode n, oldDown, newDown;

	public SetDownCommand(SkipNode n, SkipNode newDown) {
		this.n = n;
		oldDown = n.getDown();
		this.newDown = newDown;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setDown");
		e.setAttribute("key", Integer.toString(n.key));
		if (newDown != null) {
			e.setAttribute("newDown", Integer.toString(newDown.key));
		} else {
			e.setAttribute("newDown", "null");
		}
		if (oldDown != null) {
			e.setAttribute("oldDown", Integer.toString(oldDown.key));
		} else {
			e.setAttribute("oldDown", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		n.setDown(newDown);
	}

	@Override
	public void unexecute() {
		n.setDown(oldDown);
	}

}
