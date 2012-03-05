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
package algvis.scenario.commands.avlnode;

import org.jdom.Element;

import algvis.avltree.AVLNode;
import algvis.scenario.commands.Command;

public class SetBalanceCommand implements Command {
	private final AVLNode n;
	private final int fromBal, toBal;

	public SetBalanceCommand(AVLNode n, int bal) {
		this.n = n;
		this.fromBal = n.getBalance();
		this.toBal = bal;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setBalance");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("fromBalance", Integer.toString(fromBal));
		e.setAttribute("toBalance", Integer.toString(toBal));
		return e;
	}

	@Override
	public void execute() {
		n.setBalance(toBal);
	}

	@Override
	public void unexecute() {
		n.setBalance(fromBal);
	}

}
