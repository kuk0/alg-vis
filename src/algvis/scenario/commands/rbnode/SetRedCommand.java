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
package algvis.scenario.commands.rbnode;

import org.jdom.Element;

import algvis.redblacktree.RBNode;
import algvis.scenario.commands.Command;

public class SetRedCommand implements Command {
	private final boolean oldRed, newRed;
	private final RBNode n;

	public SetRedCommand(RBNode n, boolean newRed) {
		this.n = n;
		oldRed = n.isRed();
		this.newRed = newRed;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setRed");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("wasRed", Boolean.toString(oldRed));
		e.setAttribute("isRed", Boolean.toString(newRed));
		return e;
	}

	@Override
	public void execute() {
		n.setRed(newRed);
	}

	@Override
	public void unexecute() {
		n.setRed(oldRed);
	}

}
