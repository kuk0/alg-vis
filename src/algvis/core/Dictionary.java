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
package algvis.core;

import org.jdom.Element;

import algvis.scenario.Command;
import algvis.visual.Node;


abstract public class Dictionary extends DataStructure {
	public static String adtName = "dictionary";
	private Node root = null;
	private Node v = null;

	public Dictionary(VisPanel M) {
		super(M);
	}

	@Override
	abstract public void insert(int x);

	abstract public void find(int x);

	abstract public void delete(int x);

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		if (this.root != root) {
			if (scenario.isAddingEnabled()) {
				scenario.add(new SetRootCommand(root));
			}
			this.root = root;
		}
	}

	public Node getV() {
		return v;
	}

	public void setV(Node v) {
		if (this.v != v) {
			if (scenario.isAddingEnabled()) {
				scenario.add(new SetVCommand(v));
			}
			this.v = v;
		}
		if (v != null && scenario.isAddingEnabled()) {
			scenario.add(v.new WaitBackwardsCommand());
		}
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}

	private class SetRootCommand implements Command {
		private final Node newRoot, oldRoot;

		public SetRootCommand(Node newRoot) {
			oldRoot = getRoot();
			this.newRoot = newRoot;
		}

		@Override
		public void execute() {
			setRoot(newRoot);
		}

		@Override
		public void unexecute() {
			setRoot(oldRoot);
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

	private class SetVCommand implements Command {
		private final Node newV, oldV;

		public SetVCommand(Node newV) {
			oldV = getV();
			this.newV = newV;
		}

		@Override
		public void execute() {
			setV(newV);
		}

		@Override
		public void unexecute() {
			setV(oldV);
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
}
