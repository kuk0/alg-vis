package algvis.trie;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.TreeNode;
import algvis.core.View;

public class TrieNode extends TreeNode {
	public String ch; // this should be always only 1 char!
	public int radius = 2;

	public TrieNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		ch = "";
	}

	public TrieNode(DataStructure D, int key) {
		super(D, key);
		ch = "";
	}

	public TrieNode(DataStructure D, int key, String ch) {
		super(D, key);
		this.ch = ch;
	}

	public void drawEdges(View v) {
		if (state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red);
				if (getChild() != null) {
					v.drawLine(x, y, getChild().x, getChild().y);
				} else
					System.out.println("child: " + getChild());
				v.setColor(Color.black);
			} else {
				TreeNode w = getChild();
				while (w != null) {
					v.setColor(new Color(0x555555));
					v.drawLine(x, y, w.x, w.y);
					w.drawEdges(v);
					w = w.getRight();
					v.setColor(Color.black);
				}
			}
		}
	}
	
	protected void drawBg(View v) {
		v.setColor(getBgColor());
		v.fillCircle(x, y, radius);
		v.setColor(Color.BLACK); // fgcolor);
		v.drawCircle(x, y, radius);
		if (marked) {
			v.drawCircle(x, y, radius + 2);
		}
	}

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

	private void drawTrieCH(View v) {
		v.setColor(new Color(0x330000));
		v.drawString(ch, x + 3, y - radius - 10, 9);
		v.setColor(Color.black);
	}

	public void drawVertices(View v) {
		TreeNode w = getChild();
		while (w != null) {
			w.drawVertices(v);
			w = w.getRight();
		}
		draw(v);
	}

	public String getCH() {
		return ch;
	}

	public boolean hasChildWith(String ch) {
		TrieNode v = (TrieNode) this.getChild();
		if (v != null) {
			while (v != null) {
				if (ch.compareTo(v.ch) == 0) {
					return true;
				}
				v = (TrieNode) v.getRight();
			}
		}
		return false;
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
			TrieNode u = new TrieNode(D, 0, ch);
			u.setParent(getParent());
			u.setRight(this);
			getParent().setChild(u);
			return u;
		} else if (getCH().compareTo(ch) == 0) { 
			return this;
		}
		else {
			TrieNode v = (TrieNode) getRight();
			if ((v == null)) {
					TrieNode u = new TrieNode(D, 0, ch);
					u.setParent(getParent());
					setRight(u);
					return u;
			} else {
				int c = v.getCH().compareTo(ch);
				if (c > 0) {
					TrieNode u = new TrieNode(D, 0, ch);
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
			TrieNode u = new TrieNode(D, 0, ch);
			setChild(u);
			u.setParent(this);
			return u;
		} else {
			return v.addRight(ch);
		}
	}

	/**
	 * Probably useless as this is about to be visualised
	 * 
	 * @param s
	 *            A word which will be inserted after this node
	 */
	public void addWord(String s) {
		// System.out.println("addWord: " + s);

		String ch = s.substring(0, 1);
		TrieNode v = addChild(ch);
		if (s.length() > 1) {
			v.addWord(s.substring(1));
		}
	}

}
