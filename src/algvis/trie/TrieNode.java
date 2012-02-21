package algvis.trie;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.TreeNode;
import algvis.core.View;

public class TrieNode extends TreeNode {
	public int radius = 5;
	
	public TrieNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public TrieNode(DataStructure D, int key) {
		super(D, key);
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
		drawKey(v);
		drawArrow(v);
		drawArc(v);
	}

	
	public void drawVertices(View v) {
		TreeNode w = getChild();
		while (w != null) {
			w.drawVertices(v);
			w = w.getRight();
		}
		draw(v);
	}
	
}
