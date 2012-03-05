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
package algvis.scenario.commands.splay3;

import org.jdom.Element;

import algvis.scenario.commands.Command;
import algvis.splaytree.SplayNode;
import algvis.splaytree.SplayTree;

public class SetVVCommand implements Command {
	private final SplayTree T;
	private final SplayNode newVV, oldVV;

	public SetVVCommand(SplayTree T, SplayNode newVV) {
		this.T = T;
		oldVV = T.getVV();
		this.newVV = newVV;
	}

	@Override
	public void execute() {
		T.setVV(newVV);
	}

	@Override
	public void unexecute() {
		T.setVV(oldVV);
	}

	@Override
	public Element getXML() {
		Element e = new Element("setVV");
		if (newVV != null) {
			e.setAttribute("newVVKey", Integer.toString(newVV.key));
		} else {
			e.setAttribute("newVV", "null");
		}
		if (oldVV != null) {
			e.setAttribute("oldVVKey", Integer.toString(oldVV.key));
		} else {
			e.setAttribute("oldVV", "null");
		}
		return e;
	}

}
