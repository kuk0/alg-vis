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

import algvis.scenario.commands.dict.SetRootCommand;
import algvis.scenario.commands.dict.SetVCommand;
import algvis.scenario.commands.node.WaitBackwardsCommand;

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
				scenario.add(new SetRootCommand(this, root));
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
				scenario.add(new SetVCommand(this, v));
			}
			this.v = v;
		}
		if (v != null && scenario.isAddingEnabled()) {
			scenario.add(new WaitBackwardsCommand(v));
		}
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
