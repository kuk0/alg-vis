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
package algvis.scenario.commands.dict;

import org.jdom.Element;

import algvis.core.Dictionary;
import algvis.core.Node;
import algvis.scenario.commands.Command;

public class SetRootCommand implements Command {
	private final Dictionary T;
	private final Node newRoot, oldRoot;

	public SetRootCommand(Dictionary T, Node newRoot) {
		this.T = T;
		oldRoot = T.getRoot();
		this.newRoot = newRoot;
	}

	@Override
	public void execute() {
		T.setRoot(newRoot);
	}

	@Override
	public void unexecute() {
		T.setRoot(oldRoot);
	}

	@Override
	public Element getXML() {
		Element e = new Element("setRoot");
		if (newRoot != null) {
			e.setAttribute("newRootKey", Integer.toString(newRoot.key));
		} else {
			e.setAttribute("newRoot", "null");
		}
		if (oldRoot != null) {
			e.setAttribute("oldRootKey", Integer.toString(oldRoot.key));
		} else {
			e.setAttribute("oldRoot", "null");
		}
		return e;
	}

}
