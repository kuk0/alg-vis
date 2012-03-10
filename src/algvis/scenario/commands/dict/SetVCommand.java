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

public class SetVCommand implements Command {
	private final Dictionary T;
	private final Node newV, oldV;

	public SetVCommand(Dictionary T, Node newV) {
		this.T = T;
		oldV = T.getV();
		this.newV = newV;
	}

	@Override
	public void execute() {
		T.setV(newV);
	}

	@Override
	public void unexecute() {
		T.setV(oldV);
	}

	@Override
	public Element getXML() {
		Element e = new Element("setV");
		if (newV != null) {
			e.setAttribute("newVKey", Integer.toString(newV.key));
		} else {
			e.setAttribute("newV", "null");
		}
		if (oldV != null) {
			e.setAttribute("oldVKey", Integer.toString(oldV.key));
		} else {
			e.setAttribute("oldV", "null");
		}
		return e;
	}

}
