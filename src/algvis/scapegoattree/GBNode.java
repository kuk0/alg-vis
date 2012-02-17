package algvis.scapegoattree;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.scenario.commands.gbnode.SetDeletedCommand;

public class GBNode extends BSTNode {
	private boolean deleted = false;

	public GBNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public GBNode(DataStructure D, int key) {
		super(D, key);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		if (this.deleted != deleted) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetDeletedCommand(this, deleted));
			}
			this.deleted = deleted;
		}
	}

	@Override
	public GBNode getLeft() {
		return (GBNode) super.getLeft();
	}

	@Override
	public GBNode getRight() {
		return (GBNode) super.getRight();
	}

	@Override
	public GBNode getParent() {
		return (GBNode) super.getParent();
	}
}
