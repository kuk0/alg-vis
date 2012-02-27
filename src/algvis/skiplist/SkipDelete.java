package algvis.skiplist;

import algvis.core.NodeColor;
import algvis.core.Node;

public class SkipDelete extends SkipAlg {
	public SkipDelete(SkipList L, int x) {
		super(L, x);
		v.setColor(NodeColor.DELETE);
		p = new SkipNode[L.height];
		setHeader("insertion");
	}

	@Override
	public void run() {
		addStep("bstdeletestart");
		SkipNode w = find();

		if (w.getRight().key != K) {
			addStep("notfound");
			v.goDown();
			mysuspend();
			return;
		}

		L.n--;
		L.e++;
		addStep("skipdelete");
		for (int i = 0; i < L.height; ++i) {
			if (p[i].getRight().key != K) {
				break;
			}
			L.e--;
			L.setV(p[i].getRight());
			p[i].linkright(p[i].getRight().getRight());
			if (L.getV().getUp() != null) {
				L.getV().getUp().setDown(null);
			}
			L.getV().isolate();
			L.getV().goDown();
			mysuspend();
			if (i > 0 && p[i].key == -Node.INF
					&& p[i].getRight().key == Node.INF) {
				L.setRoot(p[i].getDown());
				L.sent = p[i].getRight().getDown();
				L.getRoot().setUp(null);
				L.sent.setUp(null);
				L.height = i;
				break;
			}
		}

		addStep("done");
		L.reposition();
		mysuspend();
		L.setV(null);
	}
}
