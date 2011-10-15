package algvis.skiplist;

import algvis.core.Colors;

public class SkipFind extends SkipAlg {
	public SkipFind(SkipList L, int x) {
		super(L, x);
		v.bgColor(Colors.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		setText("skipfindstart");
		SkipNode w = find();

		w = w.right;
		v.goTo(w);
		if (w.key == v.key) {
			setText("found");
			v.bgColor(Colors.FOUND);
		} else {
			setText("notfound");
			v.bgColor(Colors.NOTFOUND);
			v.goDown();
		}
		mysuspend();

		L.v = null;
	}
}
