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

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Fonts;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.TreeNode;
import algvis.core.View;

public class TrieNode extends TreeNode {
	public String ch; // this should be always only 1 char!
	public int radius = 2;
	public static final int ordinaryNode = -7;

	public boolean greyPair = false;

	public TrieNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		ch = "";
	}

	public TrieNode(DataStructure D, int key, String ch) {
		super(D, key);
		this.ch = ch;
	}

	public TrieNode(DataStructure D, int key) {
		super(D, key);
		ch = "";
	}

	public TrieNode(DataStructure D, String ch) {
		super(D, ordinaryNode);
		this.ch = ch;
	}

	public TrieNode(DataStructure D) {
		super(D, ordinaryNode);
		ch = "";
	}

	public void unsetGrey() {
		TrieNode w = (TrieNode) getChild();
		while (w != null) {
			w.unsetGrey();
			w = (TrieNode) w.getRight();
		}
		greyPair = false;
	}

	public void drawGrey(View v) {
		TrieNode w = (TrieNode) getChild();
		while (w != null) {
			w.drawGrey(v);
			w = (TrieNode) w.getRight();
		}
		if (greyPair && getParent() != null) {
			v.drawWideLine(x, y, getParent().x, getParent().y, 10.0f);
		}
	}

	@Override
	public void drawEdges(View v) {
		if (state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red);
				if (getChild() != null) {
					v.drawFancyArc(x, y, getChild().x, getChild().y);
				} else
					System.out.println("child: " + getChild());
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
		// drawKey(v);
		drawTrieCH(v);
		drawArrow(v);
		drawArc(v);
	}

	protected void drawTrieCH(View v) {
		TrieNode u = (TrieNode) getParent();
		if (u != null) {
			int midx, midy, w, h;
			if (ch.compareTo("$") == 0) {
				midx = x - ((x - u.x) / 5);
				midy = y - ((y - u.y) / 5 * 2) - 2;
				w = 4;
				h = 5;

				v.setColor(NodeColor.DARKER.bgColor);
				v.fillRoundRectangle(midx, midy, w, h, 4, 8);
				v.setColor(NodeColor.DARKER.fgColor);
				v.drawRoundRectangle(midx, midy, w, h, 4, 8);
				// if (marked) {
				// v.drawRoundRectangle(midx, midy, w + 2, h + 2, 5, 9);
				// }

				v.setColor(Color.BLACK);
				v.drawString(ch, midx, midy, Fonts.TYPEWRITER);
			} else {
				midx = x - ((x - u.x) / 7);
				midy = y - ((y - u.y) / 5 * 2) - 2;
				w = 6;
				h = 7;

				v.setColor(getBgColor());
				v.fillRoundRectangle(midx, midy, w, h, 6, 10);
				v.setColor(Color.BLACK);
				v.drawRoundRectangle(midx, midy, w, h, 6, 10);

				v.setColor(getFgColor());
				v.drawString(ch, midx, midy, Fonts.TYPEWRITER);
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
		TrieNode w = (TrieNode) getChild();
		while (w != null) {
			w.clearExtraColor();
			w = (TrieNode) w.getRight();
		}
		setColor(NodeColor.NORMAL);
	}

	public String getCH() {
		return ch;
	}

	public TrieNode getChildWithCH(String ch) {
		TrieNode v = (TrieNode) this.getChild();
		if (v != null) {
			while (v != null) {
				if (ch.compareTo(v.ch) == 0) {
					return v;
				}
				v = (TrieNode) v.getRight();
			}
		}
		return null;
	}

	@Override
	public void drawTree(View v) {
		drawGrey(v);
		super.drawTree(v);
	}

	/**
	 * 
	 * @param ch
	 *            A character which will be inserted in lexicographical order
	 * @return A node with proper "ch"
	 */
	public TrieNode addRight(String ch) {
		// System.out.println("addRight: " + this.ch + " " + ch);
		if (getCH().compareTo(ch) > 0) {
			TrieNode u = new TrieNode(D, ch);
			u.setParent(getParent());
			u.setRight(this);
			getParent().setChild(u);
			return u;
		} else if (getCH().compareTo(ch) == 0) {
			return this;
		} else {
			TrieNode v = (TrieNode) getRight();
			if ((v == null)) {
				TrieNode u = new TrieNode(D, ch);
				u.setParent(getParent());
				setRight(u);
				return u;
			} else {
				int c = v.getCH().compareTo(ch);
				if (c > 0) {
					TrieNode u = new TrieNode(D, ch);
					u.setRight(getRight());
					u.setParent(getParent());
					setRight(u);
					return u;
				} else if (c < 0) {
					return v.addRight(ch);
				} else {
					// if (c == 0)
					return v;
				}
			}
		}
	}

	/**
	 * 
	 * @param ch
	 *            A character which will be appended to this node
	 * @return A node appended
	 */
	public TrieNode addChild(String ch) {
		// System.out.println("addChild: " + ch);
		TrieNode v = (TrieNode) getChild();
		if (v == null) {
			TrieNode u = new TrieNode(D, ch);
			setChild(u);
			u.setParent(this);
			return u;
		} else {
			return v.addRight(ch);
		}
	}
}