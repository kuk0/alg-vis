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
package algvis.ds.trie;

import algvis.core.DataStructure;
import algvis.core.WordGenerator;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.Fonts;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

public class Trie extends DataStructure {
	public static String adtName = "stringology";
	public static String dsName = "trie";

	private TrieNode root = null;

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
			v.drawTree(V);
			V.drawString("\u025B", v.x, v.y-8, Fonts.NORMAL);
		}
	}

	@Override
	protected void move() {
		if (root != null) root.moveTree();
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return root == null ? null : root.getBoundingBox();
	}

	@Override
	protected void endAnimation() {
		if (root != null) root.endAnimation();
	}

	@Override
	protected boolean isAnimationDone() {
		return root == null || root.isAnimationDone();
	}

	@Override
	public void random(int n) {
		final boolean p = panel.pauses;
		panel.pauses = false;
		for (int i = 0; i < n; i++) {
			if (panel.S == null) {
				insert(WordGenerator.getSkWord());
			} else {
				insert(WordGenerator.getWord(panel.S));
			}
		}
		start(new Runnable() {
			@Override
			public void run() {
				panel.pauses = p;
			}
		});
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

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "root", root);
		if (root != null) root.storeState(state);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object root = state.get(hash + "root");
		if (root != null) this.root = (TrieNode) HashtableStoreSupport.restore(root);
		if (this.root != null) this.root.restoreState(state);
	}
}
