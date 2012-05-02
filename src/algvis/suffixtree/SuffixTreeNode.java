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

import algvis.core.DataStructure;
import algvis.core.Fonts;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.TreeNode;
import algvis.core.View;

public class SuffixTreeNode extends TreeNode {
	public int radius = 2;
	public static final int ordinaryNode = -7;
	public SuffixTreeNode suffixLink = null; // also called suffixNode
	public int from, to;

	public SuffixTreeNode(DataStructure D, int from, int to) {
		super(D, ordinaryNode);
		this.from = from;
		this.to = to;
	}

	public SuffixTreeNode(DataStructure D, int from, int to, int x, int y) {
		super(D, ordinaryNode, x, y);
		this.from = from;
		this.to = to;
	}

	public SuffixTreeNode(DataStructure D) {
		super(D, ordinaryNode);
	}

	@Override
	public void drawEdges(View v) {
		if (state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red);
				if (getChild() != null) {
					v.drawFancyArc(x, y, getChild().x, getChild().y);
				}
				v.setColor(Color.black);
			} else {
				TreeNode w = getChild();
				while (w != null) {
					v.setColor(Color.DARK_GRAY);
					v.drawFancyArc(x, y, w.x, w.y);
					w.drawEdges(v);
					w = w.getRight();
					v.setColor(Color.black);
				}
			}
		}
	}

	@Override
	protected void drawBg(View v) {
		v.setColor(Color.DARK_GRAY);
		v.fillCircle(x, y, radius);
		if (marked) {
			v.setColor(Color.BLACK);
			v.fillCircle(x, y, radius + 1);
		}
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

	protected void drawLabel(View v) {
		SuffixTreeNode u = (SuffixTreeNode) getParent();
		if (u != null) {
			int midx, midy, w, h;
			/*
			 * if (ch.compareTo("$") == 0) { midx = x - ((x - u.x) / 15); midy =
			 * y - ((y - u.y) / 5 * 2) - 1; w = 4; h = 5;
			 * 
			 * v.setColor(NodeColor.DARKER.bgColor); v.fillRoundRectangle(midx,
			 * midy, w, h, 4, 8); v.setColor(NodeColor.DARKER.fgColor);
			 * v.drawRoundRectangle(midx, midy, w, h, 4, 8); // if (marked) { //
			 * v.drawRoundRectangle(midx, midy, w + 2, h + 2, 5, 9); // }
			 * 
			 * v.setColor(Color.BLACK); v.drawString(getLabel(), midx, midy,
			 * Fonts.TYPEWRITER); } else
			 */
			{
				midx = x - ((x - u.x) / 15);
				midy = y - ((y - u.y) / 5 * 2) - 1;
				String label = getLabel();
				w = v.getGraphics().getFontMetrics((Fonts.TYPEWRITER).font)
						.stringWidth(label);
				h = 7;

				v.setColor(getBgColor());
				v.fillRoundRectangle(midx, midy, w, h, 6, 10);
				v.setColor(Color.BLACK);
				v.drawRoundRectangle(midx, midy, w, h, 6, 10);

				v.setColor(getFgColor());
				v.drawString(label, midx, midy, Fonts.TYPEWRITER);
			}
		}
	}

	@Override
	public void drawVertices(View v) {
		TreeNode w = getChild();
		while (w != null) {
			w.drawVertices(v);
			w = w.getRight();
		}
		draw(v);
	}

	public void clearExtraColor() {
		SuffixTreeNode w = (SuffixTreeNode) getChild();
		while (w != null) {
			w.clearExtraColor();
			w = (SuffixTreeNode) w.getRight();
		}
		setColor(NodeColor.NORMAL);
	}

	public SuffixTreeNode getChildWithCH(String ch) {
		SuffixTreeNode v = (SuffixTreeNode) this.getChild();
		if (v != null) {
			while (v != null) {
				if (ch.compareTo(v.getLabel()) == 0) {
					return v;
				}
				v = (SuffixTreeNode) v.getRight();
			}
		}
		return null;
	}

	@Override
	public void drawTree(View v) {
		super.drawTree(v);
	}

	/**
	 * A string will be inserted in lexicographical order
	 * 
	 * @return A node with proper string
	 */
	public SuffixTreeNode addRight(int from, int to) {
		// bullshits
		String label = ((SuffixTree) D).text.substring(from, to);
		if (getLabel().contains(label)) {
			System.out.println("Something is very wrong!!!");
			return null;
		}
		if (getLabel().compareTo(label) > 0) {
			SuffixTreeNode u = new SuffixTreeNode(D, from, to);
			u.setParent(getParent());
			u.setRight(this);
			getParent().setChild(u);
			return u;
		} else if (getLabel().compareTo(label) == 0) {
			return this;
		} else {
			SuffixTreeNode v = (SuffixTreeNode) getRight();
			if ((v == null)) {
				SuffixTreeNode u = new SuffixTreeNode(D, from, to);
				u.setParent(getParent());
				setRight(u);
				return u;
			} else {
				int c = v.getLabel().compareTo(label);
				if (c > 0) {
					SuffixTreeNode u = new SuffixTreeNode(D, from, to);
					u.setRight(getRight());
					u.setParent(getParent());
					setRight(u);
					return u;
				} else if (c < 0) {
					return v.addRight(from, to);
				} else {
					return v;
				}
			}
		}
	}

	public SuffixTreeNode childContaining(String label) {
		if (isLeaf()) {
			return null;
		} else {
			SuffixTreeNode v = (SuffixTreeNode) getChild();
			while (v != null) {
				if (v.getLabel().startsWith(label))
					return v;
				v = (SuffixTreeNode) v.getRight();
			}
			return null;
		}
	}

	public String getLabel() {
		return ((SuffixTree) D).text.substring(from, to);
	}

	public void setLabel(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public SuffixTreeNode splitEdge(int length) {
		SuffixTreeNode parent = (SuffixTreeNode) this.getParent();
		SuffixTreeNode result = new SuffixTreeNode(this.D, from, from + length);
		setLabel(from + length, to);
		parent.deleteChild(this);
		parent.addChild(result);
		result.addChild(this);

		return result;
	}
}
