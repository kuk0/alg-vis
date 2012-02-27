package algvis.skiplist;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;
import algvis.scenario.commands.skipnode.SetDownCommand;
import algvis.scenario.commands.skipnode.SetLeftCommand;
import algvis.scenario.commands.skipnode.SetRightCommand;
import algvis.scenario.commands.skipnode.SetUpCommand;

public class SkipNode extends Node {
	private SkipNode left = null;
	private SkipNode right = null;
	private SkipNode up = null;
	private SkipNode down = null;

	public SkipNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public SkipNode(DataStructure D, int key) {
		super(D, key);
	}

	public void linkleft(SkipNode v) {
		setLeft(v);
		v.setRight(this);
	}

	public void linkright(SkipNode v) {
		setRight(v);
		v.setLeft(this);
	}

	public void linkup(SkipNode v) {
		setUp(v);
		v.setDown(this);
	}

	public void linkdown(SkipNode v) {
		setDown(v);
		v.setUp(this);
	}

	public void isolate() {
		setLeft(setRight(setUp(setDown(null))));
	}

	@Override
	public void drawBg(View v) {
		v.setColor(getBgColor());
		v.fillSqr(x, y, Node.radius);
		v.setColor(Color.BLACK); // fgcolor);
		v.drawSqr(x, y, Node.radius);
		if (marked) {
			v.drawSqr(x, y, Node.radius + 2);
		}
	}

	public void drawSkipList(View V) {
		if (getLeft() == null && getDown() != null) {
			V.setColor(Color.black);
			V.drawLine(x, y, getDown().x, getDown().y);
			getDown().drawSkipList(V);
		}
		if (getRight() != null) {
			V.setColor(Color.black);
			V.drawArrow(x, y, getRight().x - Node.radius, getRight().y);
			getRight().drawSkipList(V);
		}
		draw(V);
	}

	public void moveSkipList() {
		if (getLeft() == null && getDown() != null) {
			getDown().moveSkipList();
		}
		if (getRight() != null) {
			getRight().moveSkipList();
		}
		move();
	}

	public void _reposition() {
		if (D.x2 < this.tox) {
			D.x2 = this.tox;
		}
		if (D.y2 < this.toy) {
			D.y2 = this.toy;
		}
		if (getLeft() == null) {
			if (getUp() == null) {
				goToRoot();
			} else {
				goTo(getUp().tox, getUp().toy + DataStructure.minsepy);
			}
			if (getDown() != null) {
				getDown()._reposition();
			}
		} else {
			if (getDown() == null) {
				goNextTo(getLeft());
			} else {
				goTo(getDown().tox, getLeft().toy);
			}
		}
		if (getRight() != null) {
			getRight()._reposition();
		}
	}

	public SkipNode find(int x, int y) {
		if (inside(x, y))
			return this;
		if (getLeft() == null && getDown() != null) {
			SkipNode tmp = getDown().find(x, y);
			if (tmp != null)
				return tmp;
		}
		if (getRight() != null) {
			return getRight().find(x, y);
		}
		return null;
	}

	public SkipNode getLeft() {
		return left;
	}

	public void setLeft(SkipNode left) {
		if (this.left != left) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetLeftCommand(this, left));
			}
			this.left = left;
		}
	}

	public SkipNode getRight() {
		return right;
	}

	public SkipNode setRight(SkipNode right) {
		if (this.right != right) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetRightCommand(this, right));
			}
			this.right = right;
		}
		return right;
	}

	public SkipNode getUp() {
		return up;
	}

	public SkipNode setUp(SkipNode up) {
		if (this.up != up) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetUpCommand(this, up));
			}
			this.up = up;
		}
		return up;
	}

	public SkipNode getDown() {
		return down;
	}

	public SkipNode setDown(SkipNode down) {
		if (this.down != down) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetDownCommand(this, down));
			}
			this.down = down;
		}
		return down;
	}
}
