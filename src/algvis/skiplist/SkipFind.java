package algvis.skiplist;

import algvis.core.NodeColor;

public class SkipFind extends SkipAlg {
	public SkipFind(SkipList L, int x) {
		super(L, x);
		v.setColor(NodeColor.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		addStep("skipfindstart");
		SkipNode w = find();

		w = w.right;
		v.goTo(w);
		if (w.key == v.key) {
			addStep("found");
			v.setColor(NodeColor.FOUND);
		} else {
			addStep("notfound");
			v.setColor(NodeColor.NOTFOUND);
			v.goDown();
		}
		mysuspend();

		L.v = null;
	}
}
