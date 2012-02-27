package algvis.core;

import algvis.scenario.commands.dict.SetRootCommand;
import algvis.scenario.commands.dict.SetVCommand;
import algvis.scenario.commands.node.WaitBackwardsCommand;

abstract public class Dictionary extends DataStructure {
	public static String adtName = "dictionary";
	private Node root = null;
	private Node v = null;

	public Dictionary(VisPanel M) {
		super(M);
	}

	@Override
	abstract public void insert(int x);

	abstract public void find(int x);

	abstract public void delete(int x);

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		if (this.root != root) {
			if (scenario.isAddingEnabled()) {
				scenario.add(new SetRootCommand(this, root));
			}
			this.root = root;
		}
	}

	public Node getV() {
		return v;
	}

	public void setV(Node v) {
		if (this.v != v) {
			if (scenario.isAddingEnabled()) {
				scenario.add(new SetVCommand(this, v));
			}
			this.v = v;
		}
		if (v != null && scenario.isAddingEnabled()) {
			scenario.add(new WaitBackwardsCommand(v));
		}
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
