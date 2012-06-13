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
package algvis.scenario;

import org.jdom.Element;

import algvis.core.DataStructure;
import algvis.core.Node;

public interface Command extends XMLable {

	public void execute();

	public void unexecute();
	
	public static class SetNodeCommand implements Command {
		private final DataStructure D;
		private final int i;
		private final Node newNode, oldNode;

		public SetNodeCommand(DataStructure D, int i, Node oldNode, Node newNode) {
			this.D = D;
			this.i = i;
			this.oldNode = oldNode;
			this.newNode = newNode;
		}

		@Override
		public void execute() {
			D.setNode(i, newNode, false);
		}

		@Override
		public void unexecute() {
			D.setNode(i, oldNode, false);
		}

		@Override
		public Element getXML() {
			Element e = new Element("setNode");
			e.setAttribute("DS", D.getClass().getName());
			e.setAttribute("orderOfNode", Integer.toString(i));
			if (newNode != null) {
				e.setAttribute("newNodeKey", Integer.toString(newNode.getKey()));
			} else {
				e.setAttribute("newNode", "null");
			}
			if (oldNode != null) {
				e.setAttribute("oldNodeKey", Integer.toString(oldNode.getKey()));
			} else {
				e.setAttribute("oldNode", "null");
			}
			return e;
		}
	}
}
