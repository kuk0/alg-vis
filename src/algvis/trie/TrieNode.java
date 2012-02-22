package algvis.trie;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.TreeNode;
import algvis.core.View;

public class TrieNode extends TreeNode {
	public String ch; // this should be always only 1 char!
	public int radius = 2;
	public static final int ordinaryNode = -1;
	public static final Color myGrey = new Color(0x474747);
	public static final Color myC = new Color(0x470000);

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
					v.setColor(myGrey);
					v.drawLine(x, y, w.x, w.y);
					w.drawEdges(v);
					w = w.getRight();
					v.setColor(Color.black);
				}
			}
		}
	}

	protected void drawBg(View v) {
		v.setColor(myGrey);
		// v.setColor(getBgColor());
		v.fillCircle(x, y, radius);
		v.setColor(myGrey);
		// v.setColor(Color.BLACK); // fgcolor);
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
		drawTrieEOW(v);
		drawArrow(v);
		drawArc(v);
	}

	private void drawTrieEOW(View v) {
		if (key != ordinaryNode) {
			v.setColor(myGrey);
			Pair from = new Pair(x + radius / 4 * 7, y + radius / 4 * 7);
			@SuppressWarnings("static-access")
			Pair to = new Pair(x + radius + (D.minsepx / 5), y + radius + 1);
			int h = 3;
			int w = 2;
			Pair at = new Pair(to.first + w / 2 + 1, to.second + h / 2 + 2);
			// v.drawArrow(from.first, from.second, to.first, to.second);
			v.drawLine(from.first, from.second, to.first, to.second);
			v.setColor(new Color(0xf00000));
			v.fillRoundRectangle(at.first, at.second, w, h, 0, 0);
			v.setColor(myGrey);
			v.drawRoundRectangle(at.first, at.second, w, h, 0, 0);
		}

	}

	private void drawTrieCH(View v) {
		TrieNode u = (TrieNode) getParent();
		if (u != null) {
			int midx = x - ((x - u.x) / 3);
			int midy = y - ((y - u.y) / 3) - 2;
			int w = 6;
			int h = 7;

			v.setColor(Color.YELLOW);
			v.fillRoundRectangle(midx, midy, w, h, 6, 10);
			v.setColor(Color.BLACK);
			v.drawRoundRectangle(midx, midy, w, h, 6, 10);

			v.setColor(myC);
			v.drawString(ch, midx, midy, 9);
			v.setColor(Color.BLACK);
		}
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

	public TrieNode childWithCH(String ch) {
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
		} else {
			v.key = 1;
		}
	}

	public TrieNode find(String s) {
		if (s.length() == 0) {
			if (key != ordinaryNode) {
				return this;
			} else {
				return null;
			}
		} else {
			String ch = s.substring(0, 1);

			TrieNode v = childWithCH(ch);
			if (v == null) {
				return null;
			} else {
				return v.find(s.substring(1));
			}
		}
	}

	public void deleteDeadBranch() {
		TrieNode v = (TrieNode) getParent();
		if ((v != null) && (getChild() == null)) {
			v.deleteChild(this);
			v.deleteDeadBranch();
		}
	}

	public void deleteWord(String s) {
		TrieNode v = find(s);
		if (v != null) {
			v.key = ordinaryNode;
			v.deleteDeadBranch();
		}
	}
}
