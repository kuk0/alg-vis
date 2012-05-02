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
package algvis.trie;

import algvis.core.DataStructure;
import algvis.core.Fonts;
import algvis.core.View;
import algvis.core.VisPanel;
import algvis.core.WordGenerator;

public class Trie extends DataStructure {
	public static String adtName = "stringology";
	public static String dsName = "trie";

	private TrieNode root = null;
	private TrieNode v = null;

	public TrieWordNode hw = null;

	// public final static String EPSILON = "\u025B";

	public Trie(VisPanel M) {
		super(M);
		clear();
	}

	@Override
	public String getName() {
		return "trie";
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void insert(int x) {
		//
	}

	@Override
	public void clear() {
		root = new TrieNode(this);
		root.reposition();
		v = null;
	}

	public TrieNode getV() {
		return v;
	}

	public TrieNode setV(TrieNode v) {
		this.v = v;
		return v;
	}

	public TrieNode getRoot() {
		return root;
	}

	public void setRoot(TrieNode root) {
		this.root = root;
	}

	@Override
	public void draw(View V) {
		TrieNode v = getRoot();
		if (v != null) {
			v.moveTree();
			v.drawTree(V);
			V.drawString("\u025B", v.x, v.y-8, Fonts.NORMAL);
		}
		v = getV();
		if (v != null) {
			v.move();
			v.drawLabel(V);
		}
		if (hw != null) {
			hw.move();
			hw.draw(V);
		}
	}

	@Override
	public void random(int n) {
		boolean p = M.pause;
		M.pause = false;
		for (int i = 0; i < n; i++) {
			if (M.S == null) {
				insert(WordGenerator.getSkWord());
			} else {
				insert(WordGenerator.getWord(M.S));
			}
		}
		M.pause = p;
	}

	public void insert(String s) {
		start(new TrieInsert(this, s));
	}

	public void find(String s) {
		start(new TrieFind(this, s));
	}

	public void delete(String s) {
		start(new TrieDelete(this, s));
	}

	public void reposition() {
		getRoot().reposition();
	}

	public void clearExtraColor() {
		TrieNode r = getRoot();
		if (r != null) {
			getRoot().clearExtraColor();
		}
	}

}
