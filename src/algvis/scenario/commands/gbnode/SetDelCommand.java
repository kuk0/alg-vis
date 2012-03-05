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

import algvis.scapegoattree.GBTree;
import algvis.scenario.commands.Command;

public class SetDelCommand implements Command {
	private final GBTree T;
	private final int oldDel, newDel;

	public SetDelCommand(GBTree T, int newDel) {
		this.T = T;
		oldDel = T.getDel();
		this.newDel = newDel;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setDel");
		e.setAttribute("GBTree", Integer.toString(T.hashCode()));
		e.setAttribute("oldDel", Integer.toString(oldDel));
		e.setAttribute("newDel", Integer.toString(newDel));
		return e;
	}

	@Override
	public void execute() {
		T.setDel(newDel);
	}

	@Override
	public void unexecute() {
		T.setDel(oldDel);
	}

}
