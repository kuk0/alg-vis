package algvis.skiplist;

import algvis.core.Node;

public class SkipDelete extends SkipAlg {
	public SkipDelete(SkipList L, int x) {
		super(L, x);
		v.bgColor(Node.DELETE);
		p = new SkipNode[L.height];
		setHeader("insertion");
	}

	@Override
	public void run() {
		setText("bstdeletestart");
		SkipNode w = find();
		
		if (w.right.key != K) {
			setText("notfound");
			v.goDown();
			mysuspend();
			return;
		}

		L.n--;
		L.e++;
		setText("skipdelete");
		for (int i = 0; i < L.height; ++i) {
			if (p[i].right.key != K) {
				break;
			}
			L.e--;
			L.v = p[i].right;
			p[i].linkright(p[i].right.right);
			if (L.v.up != null) {
				L.v.up.down = null;
			}
			L.v.isolate();
			L.v.goDown();
			mysuspend();
			if (i > 0 && p[i].key == -Node.INF && p[i].right.key == Node.INF) {
				L.root = p[i].down;
				L.sent = p[i].right.down;
				L.root.up = null;
				L.sent.up = null;
				L.height = i;
				break;
			}
		}

		setText("done");
		L.reposition();
		mysuspend();
		L.v = null;
	}
}
