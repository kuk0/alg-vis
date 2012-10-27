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
package algvis.ds.suffixtree;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import algvis.core.DataStructure;
import algvis.core.StringElem;
import algvis.core.WordGenerator;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.Fonts;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

public class SuffixTree extends DataStructure {
	public static final int textpos = -40;
	public static String adtName = "stringology";
	public static String dsName = "suffixtree";

	private SuffixTreeNode root = null;

	public String text;
	StringElem str;

	public SuffixTree(VisPanel M) {
		super(M);
		clear();
	}

	public SuffixTree(VisPanel M, String text) {
		super(M);
		clear();
		this.text = text;
		this.str = new StringElem(this, text, 0, textpos);
	}

	@Override
	public String getName() {
		return "suffixtree";
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void insert(int x) {}

	@Override
	public void clear() {
		root = new SuffixTreeNode(this);
		root.reposition();
		str = null;
	}

	public SuffixTreeNode getRoot() {
		return root;
	}

	public void setRoot(SuffixTreeNode root) {
		this.root = root;
	}

	@Override
	public void draw(View V) {
		if (str != null)
			str.draw(V);
		SuffixTreeNode v = getRoot();
		if (v != null) {
			v.drawTree(V);
			V.drawString("\u025B", v.x, v.y - 8, Fonts.NORMAL);
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
		start(new SuffixTreeInsert(this, s));
	}

	public void find(String s) {
		start(new SuffixTreeFind(this, s));
	}

	public void reposition() {
		getRoot().reposition();
	}

	public void clearExtraColor() {
		SuffixTreeNode r = getRoot();
		if (r != null) {
			getRoot().clearExtraColor();
		}
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "root", root);
		if (root != null) root.storeState(state);
		HashtableStoreSupport.store(state, hash + "text", text);
		HashtableStoreSupport.store(state, hash + "str", str);
		if (str != null) str.storeState(state);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object root = state.get(hash + "root");
		if (root != null) this.root = (SuffixTreeNode) HashtableStoreSupport.restore(root);
		if (this.root != null) this.root.restoreState(state);
		Object text = state.get(hash + "text");
		if (text != null) this.text = (String) HashtableStoreSupport.restore(text);
		Object str = state.get(hash + "str");
		if (str != null) this.str = (StringElem) HashtableStoreSupport.restore(str);
		
		if (this.str != null) this.str.restoreState(state);
	}
}
