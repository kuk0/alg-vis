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
package algvis.scenario.commands.gbnode;

import org.jdom.Element;

import algvis.scapegoattree.GBNode;
import algvis.scenario.commands.Command;

public class SetDeletedCommand implements Command {
	private final GBNode n;
	private final boolean oldDeleted, newDeleted;

	public SetDeletedCommand(GBNode n, boolean newDeleted) {
		this.n = n;
		oldDeleted = n.isDeleted();
		this.newDeleted = newDeleted;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setDeleted");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("wasDeleted", Boolean.toString(oldDeleted));
		e.setAttribute("isDeleted", Boolean.toString(newDeleted));
		return e;
	}

	@Override
	public void execute() {
		n.setDeleted(newDeleted);
	}

	@Override
	public void unexecute() {
		n.setDeleted(oldDeleted);
	}

}
