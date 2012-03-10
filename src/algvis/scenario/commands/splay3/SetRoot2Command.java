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

public class SetRoot2Command implements Command {
	private final SplayTree T;
	private final SplayNode oldRoot2, newRoot2;

	public SetRoot2Command(SplayTree T, SplayNode newRoot2) {
		this.T = T;
		oldRoot2 = T.getRoot2();
		this.newRoot2 = newRoot2;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setRoot2");
		if (newRoot2 != null) {
			e.setAttribute("newRoot2Key", Integer.toString(newRoot2.key));
		} else {
			e.setAttribute("newRoot2", "null");
		}
		if (oldRoot2 != null) {
			e.setAttribute("oldRoot2Key", Integer.toString(oldRoot2.key));
		} else {
			e.setAttribute("oldRoot2", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		T.setRoot2(newRoot2);
	}

	@Override
	public void unexecute() {
		T.setRoot2(oldRoot2);
	}

}
