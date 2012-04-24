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

abstract public class Dictionary extends DataStructure {
	public static String adtName = "dictionary";

	public Dictionary(VisPanel M) {
		super(M, 2); // root (0), v (1)
	}

	@Override
	abstract public void insert(int x);

	abstract public void find(int x);

	abstract public void delete(int x);

	public Node getRoot() {
		return nodes.get(0);
	}

	public void setRoot(Node root) {
		setNode(0, root, false);
	}

	public Node getV() {
		return nodes.get(1);
	}

	public void setV(Node v) {
		setNode(1, v, true);
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}

}
