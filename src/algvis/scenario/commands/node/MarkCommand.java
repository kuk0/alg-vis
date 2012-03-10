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

public class MarkCommand implements Command {
	private final Node n;
	private final boolean marked;

	public MarkCommand(Node n, boolean marked) {
		this.n = n;
		this.marked = marked;
	}

	@Override
	public Element getXML() {
		Element e = new Element("mark");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("marked", Boolean.toString(marked));
		return e;
	}

	@Override
	public void execute() {
		if (marked) {
			n.mark();
		} else {
			n.unmark();
		}
	}

	@Override
	public void unexecute() {
		if (marked) {
			n.unmark();
		} else {
			n.mark();
		}
	}

}
