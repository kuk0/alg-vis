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

import algvis.core.StringElem;
import algvis.core.WordGenerator;
import algvis.ds.DataStructure;
import algvis.ds.trie.TrieWordNode;
import algvis.gui.Fonts;
import algvis.gui.VisPanel;
import algvis.gui.view.View;

import java.awt.geom.Rectangle2D;

public class SuffixTree extends DataStructure {
	private static final int textpos = -40;
	public static String adtName = "stringology";
	public static String dsName = "suffixtree";

	private SuffixTreeNode root = null;
	private SuffixTreeNode v = null;

	public TrieWordNode hw = null;
	private final TrieWordNode cs = null;

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
		v = null;
		str = null;
	}

	SuffixTreeNode getV() {
		return v;
	}

	public SuffixTreeNode setV(SuffixTreeNode v) {
		this.v = v;
		return v;
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
			v.moveTree();
			v.drawTree(V);
			V.drawString("\u025B", v.x, v.y - 8, Fonts.NORMAL);
		}
		v = getV();
		if (v != null) {
			v.move();
			v.drawLabel(V);
		}
		if (hw != null) {
			hw.draw(V);
		}
		if (cs != null) {
			cs.draw(V);
		}
	}

	@Override
	protected void move() {
		// TODO
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return null; // TODO
	}

	@Override
	protected void endAnimation() {
		// TODO
	}

	@Override
	protected boolean isAnimationDone() {
		return false; // TODO
	}

	@Override
	public void random(int n) {
		boolean p = panel.pauses;
		panel.pauses = false;
		for (int i = 0; i < n; i++) {
			if (panel.S == null) {
				insert(WordGenerator.getSkWord());
			} else {
				insert(WordGenerator.getWord(panel.S));
			}
		}
		panel.pauses = p;
	}

	public void insert(String s) {
		text = s;
		str = new StringElem(this, text, 0, textpos);
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
}
