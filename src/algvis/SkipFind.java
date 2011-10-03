package algvis;

public class SkipFind extends SkipAlg {
	public SkipFind(SkipList L, int x) {
		super(L, x);
		v.bgColor(Node.FIND);
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
			v.bgColor(Node.FOUND);
		} else {
			setText("notfound");
			v.bgColor(Node.NOTFOUND);
			v.goDown();
		}
		mysuspend();

		L.v = null;
	}
}
