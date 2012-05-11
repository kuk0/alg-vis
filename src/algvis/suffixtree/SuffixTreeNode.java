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
package algvis.suffixtree;

import java.awt.Color;
import java.awt.geom.Point2D;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;
import algvis.trie.TrieNode;

public class SuffixTreeNode extends TrieNode {
	private SuffixTreeNode suffixLink = null; // also called suffixNode
	private boolean packed;

	public SuffixTreeNode(DataStructure D, int key, int x, int y, boolean packed) {
		super(D, key, x, y);
		setPacked(packed);
	}

	public SuffixTreeNode(DataStructure D, int key, String ch, boolean packed) {
		super(D, key, ch);
		setPacked(packed);
	}

	public SuffixTreeNode(DataStructure D, int key, boolean packed) {
		super(D, key);
		setPacked(packed);
	}

	public SuffixTreeNode(DataStructure D, String ch, int x, int y,
			boolean packed) {
		super(D, ch, x, y);
		setPacked(packed);
	}

	public SuffixTreeNode(DataStructure D, String ch, boolean packed) {
		super(D, ch);
		setPacked(packed);
	}

	public SuffixTreeNode(DataStructure D, boolean packed) {
		super(D);
		setPacked(packed);
	}

	public SuffixTreeNode(DataStructure D) {
		super(D);
		setPacked(false);
	}

	public boolean isPacked() {
		return packed;
	}

	public void setPacked(boolean packed) {
		this.packed = packed;
	}

	@Override
	protected void drawBg(View v) {
		super.drawBg(v);
		if (isPacked()) {
			v.setColor(Color.WHITE);
			v.fillCircle(x, y, radius - 1);
		}
	}

	public SuffixTreeNode addRight(String ch, int x, int y, boolean packed) {
		if (getLabel().compareTo(ch) > 0) {
			SuffixTreeNode u = new SuffixTreeNode(D, ch, packed);
			u.setParent(getParent());
			u.setRight(this);
			getParent().setChild(u);
			return u;
		} else if (getLabel().compareTo(ch) == 0) {
			return this;
		} else {
			SuffixTreeNode v = (SuffixTreeNode) getRight();
			if ((v == null)) {
				SuffixTreeNode u = new SuffixTreeNode(D, ch, packed);
				u.setParent(getParent());
				setRight(u);
				return u;
			} else {
				int c = v.getLabel().compareTo(ch);
				if (c > 0) {
					SuffixTreeNode u = new SuffixTreeNode(D, ch, packed);
					u.setRight(getRight());
					u.setParent(getParent());
					setRight(u);
					return u;
				} else if (c < 0) {
					return v.addRight(ch, x, y, packed);
				} else {
					// if (c == 0)
					return v;
				}
			}
		}
	}

	public TrieNode addChild(String ch, int x, int y, boolean packed) {
		SuffixTreeNode v = (SuffixTreeNode) getChild();
		if (v == null) {
			SuffixTreeNode u = new SuffixTreeNode(D, ch, x, y, packed);
			setChild(u);
			u.setParent(this);
			return u;
		} else {
			return v.addRight(ch, x, y, packed);
		}
	}

	public SuffixTreeNode getSuffixLink() {
		return suffixLink;
	}

	public void setSuffixLink(SuffixTreeNode suffixLink) {
		this.suffixLink = suffixLink;
	}

	@Override
	public void draw(View v) {
		if (state == Node.INVISIBLE || key == NULL) {
			return;
		}
		drawBg(v);
		drawLabel(v);
		drawArrow(v);
		drawArc(v);
	}

	public void drawSuffixLinks(View v) {
		SuffixTreeNode child = (SuffixTreeNode) getChild();
		while (child != null) {
			child.drawSuffixLinks(v);
			child = (SuffixTreeNode) child.getRight();
		}
		
		if (getSuffixLink() != null) {
			if (isPacked()) {
				v.setColor(new Color(0xffaaaa));
			} else {
				v.setColor(new Color(0xcccccc));
			}
			SuffixTreeNode w = getSuffixLink();
			Point2D p = v.cut(x,y, w.x, w.y, 10);
			v.drawArrow(x, y, (int)p.getX(), (int)p.getY());
			v.setColor(Color.BLACK);
		}
	}

	@Override
	public void drawTree(View v) {
		drawSuffixLinks(v);
		super.drawTree(v);
	}
}
