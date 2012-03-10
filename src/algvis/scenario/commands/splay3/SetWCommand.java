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

public class SetWCommand implements Command {
	private final SplayTree T;
	private final SplayNode oldW, newW;
	private final int order;

	public SetWCommand(SplayTree T, SplayNode newW, int order) {
		this.T = T;
		this.order = order;
		switch (order) {
		case 1:
			oldW = T.getW1();
			break;
		case 2:
			oldW = T.getW2();
			break;
		default:
			oldW = null;
			System.err
					.println("SetWCommand bad \"order\" argument (must be 1 or 2)");
		}
		this.newW = newW;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setW" + order);
		if (newW != null) {
			e.setAttribute("newWKey", Integer.toString(newW.key));
		} else {
			e.setAttribute("newW", "null");
		}
		if (oldW != null) {
			e.setAttribute("oldWKey", Integer.toString(oldW.key));
		} else {
			e.setAttribute("oldW", "null");
		}
		return e;
	}

	@Override
	public void execute() {
		if (order == 1) {
			T.setW1(newW);
		} else {
			T.setW2(newW);
		}
	}

	@Override
	public void unexecute() {
		if (order == 1) {
			T.setW1(oldW);
		} else {
			T.setW2(oldW);
		}
	}

}
