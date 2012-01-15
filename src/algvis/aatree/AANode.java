package algvis.aatree;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;

public class AANode extends BSTNode {
	int level = 1;

	public AANode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
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
		String str = new String("" + level);
		v.drawString(str, x + D.radius, y - D.radius, 7);
	}

	@Override
	public void rebox() {
		leftw = (left == null) ? D.xspan + D.radius : left.leftw + left.rightw;
		rightw = (right == null) ? D.xspan + D.radius : right.leftw
				+ right.rightw;
	}

	@Override
	public void reboxTree() {
		if (left != null) {
			left.reboxTree();
		}
		if (right != null) {
			right.reboxTree();
		}
		rebox();
	}

	private void repos() {
		if (isRoot()) {
			goToRoot();
			D.x1 = -leftw;
			D.x2 = rightw;
			D.y2 = this.toy;
		}
		if (this.toy > D.y2) {
			D.y2 = this.toy;
		}
		if (left != null) {
			if (((AA) D).mode23) {
				left.goTo(this.tox - left.rightw, this.toy
						+ (((AANode) left).level == level ? D.yspan : 2
								* D.radius + D.yspan));
			} else {
				left.goTo(this.tox - left.rightw, this.toy + 2 * D.radius
						+ D.yspan);
			}
			((AANode) left).repos();
		}
		if (right != null) {
			if (((AA) D).mode23) {
				right.goTo(this.tox + right.leftw, this.toy
						+ (((AANode) right).level == level ? D.yspan : 2
								* D.radius + D.yspan));
			} else {
				right.goTo(this.tox + right.leftw, this.toy + 2 * D.radius
						+ D.yspan);
			}
			((AANode) right).repos();
		}
	}

	@Override
	public void reposition() {
		reboxTree();
		repos();
	}
}
