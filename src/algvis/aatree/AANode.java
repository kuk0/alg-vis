package algvis.aatree;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;

public class AANode extends BSTNode {
	public AANode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		setLevel(1);
	}

	public AANode(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	public AANode(BSTNode v) {
		this(v.D, v.key, v.x, v.y);
	}

	@Override
	public void draw(View v) {
		if (state == Node.INVISIBLE || key == NULL) {
			return;
		}
		drawBg(v);
		drawKey(v);
		drawArrow(v);
		drawArc(v);
		String str = new String("" + getLevel());
		v.drawString(str, x + D.radius, y - D.radius, 7);
	}

	public void drawBigNodes(View v) {
		if (left != null) {
			((AANode) left).drawBigNodes(v);
		}
		if (right != null) {
			((AANode) right).drawBigNodes(v);
		}
		if (parent != null && parent.getLevel() == getLevel()) {
			v.drawWideLine(x, y, parent.x, parent.y);
		} else {
			v.drawWideLine(x - 1, y, x + 1, y);
		}
	}
	
	public void drawTree2(View v) {
		if (((AA)D).mode23) drawBigNodes(v);
		drawTree(v);
	}
}
