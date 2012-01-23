package algvis.redblacktree;

import java.awt.Color;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;

public class RBNode extends BSTNode {
	boolean red = true;

	public RBNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public RBNode(DataStructure D, int key) {
		super(D, key);
	}

	@Override
	public RBNode getLeft() {
		return (RBNode) super.getLeft();
	}

	@Override
	public RBNode getRight() {
		return (RBNode) super.getRight();
	}

	@Override
	public RBNode getParent() {
		return (RBNode) super.getParent();
	}

	@Override
	public boolean isRoot() {
		return getParent().key == Node.NULL;
	}

	@Override
	public boolean isLeaf() {
		return getLeft().key == Node.NULL && getRight().key == Node.NULL;
	}

	@Override
	public boolean isLeft() {
		return getParent().getLeft() == this;
	}

	@Override
	public void linkLeft(BSTNode v) {
		setLeft(v);
		if (v.key != Node.NULL) {
			v.setParent(this);
		}
	}

	@Override
	public void linkRight(BSTNode v) {
		setRight(v);
		if (v.key != Node.NULL) {
			v.setParent(this);
		}
	}

	@Override
	public void isolate() {
		setLeft(setRight(setParent(((RB) D).NULL)));
	}

	@Override
	public void draw(View v) {
		if (state == Node.INVISIBLE || key == NULL) {
			return;
		}
		bgColor(red ? Color.red : Color.black);
		fgColor(red ? Color.black : Color.white);
		super.draw(v);
	}

	public void drawBigNodes(View v) {
		if (key == NULL) {
			return;
		}
		if (getLeft() != null) {
			getLeft().drawBigNodes(v);
		}
		if (getRight() != null) {
			getRight().drawBigNodes(v);
		}
		if (red && getParent() != null) {
			v.drawWideLine(x, y, getParent().x, getParent().y);
		} else {
			v.drawWideLine(x - 1, y, x + 1, y);
		}
	}

	@Override
	public void calc() {
		if (key == NULL) {
			return;
		}
		int ls = 0, rs = 0, lh = 0, rh = 0, lsh = 0, rsh = 0;
		if (getLeft() != null) {
			ls = getLeft().size;
			lh = getLeft().height;
			lsh = getLeft().sumh;
		}
		if (getRight() != null) {
			rs = getRight().size;
			rh = getRight().height;
			rsh = getRight().sumh;
		}
		size = ls + rs + 1;
		height = Math.max(lh, rh) + 1;
		sumh = lsh + rsh + size;
	}

	@Override
	public void calcTree() {
		if (key == Node.NULL) {
			return;
		}
		if (getLeft().key != Node.NULL) {
			getLeft().calcTree();
		}
		if (getRight().key != Node.NULL) {
			getRight().calcTree();
		}
		calc();
	}

	public void drawTree2(View v) {
		if (((RB) D).mode24) {
			drawBigNodes(v);
		}
		drawTree(v);
	}

	@Override
	public void moveTree() {
		if (getLeft().key != Node.NULL) {
			getLeft().moveTree();
		}
		if (getRight().key != Node.NULL) {
			getRight().moveTree();
		}
		move();
	}

	// */@Override
	@Override
	public void rebox() {
		leftw = (getLeft().key == Node.NULL) ? D.xspan + D.radius
				: getLeft().leftw + getLeft().rightw;
		rightw = (getRight().key == Node.NULL) ? D.xspan + D.radius
				: getRight().leftw + getRight().rightw;
	}

	@Override
	public void reboxTree() {
		if (getLeft().key != Node.NULL) {
			getLeft().reboxTree();
		}
		if (getRight().key != Node.NULL) {
			getRight().reboxTree();
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
		if (getLeft().key != Node.NULL) {
			if (((RB) D).mode24) {
				getLeft().goTo(
						this.tox - getLeft().rightw,
						this.toy
								+ (getLeft().red ? D.yspan : 2 * D.radius
										+ D.yspan) - (red ? D.yspan : 0));
			} else {
				getLeft().goTo(this.tox - getLeft().rightw,
						this.toy + 2 * D.radius + D.yspan);
			}
			getLeft().repos();
		}
		if (getRight().key != Node.NULL) {
			if (((RB) D).mode24) {
				getRight().goTo(
						this.tox + getRight().leftw,
						this.toy
								+ (getRight().red ? D.yspan : 2 * D.radius
										+ D.yspan) - (red ? D.yspan : 0));
			} else {
				getRight().goTo(this.tox + getRight().leftw,
						this.toy + 2 * D.radius + D.yspan);
			}
			getRight().repos();
		}
	}

	@Override
	public void reposition() {
		if (key != NULL) {
			reboxTree();
			repos();
		}
	} // */
}
