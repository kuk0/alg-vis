package algvis.skiplist;

import java.util.Random;

import algvis.core.NodeColor;
import algvis.core.Node;

public class SkipInsert extends SkipAlg {
	Random R;

	public SkipInsert(SkipList L, int x) { // Buttons B,
		super(L, x);
		v.setColor(NodeColor.INSERT);
		setHeader("insertion");
		R = new Random();
	}

	@Override
	public void run() {
		addStep("skipinsertstart");
		SkipNode w = find();

		if (w.getRight().key == v.key) {
			addStep("alreadythere");
			v.goDown();
			mysuspend();
			// System.out.println("dupl");
			return;
		}

		L.n++;
		addStep("skipinsertafter");
		SkipNode z, oldv = null;
		int i = 0;
		do {
			if (i > 0) {
				addStep("skippromote");
				L.e++;
			}
			if (i < L.height) {
				w = p[i++];
				z = w.getRight();
				w.linkright(v);
				z.linkleft(v);
				if (oldv != null) {
					v.linkdown(oldv);
				}
				L.reposition();
				oldv = v;
				v = new SkipNode(L, v.key, v.tox, -10);
			} else {
				v.linkdown(oldv);
				SkipNode oldr = L.getRoot(), olds = L.sent;
				v.linkleft(L.setRoot(new SkipNode(L, -Node.INF)));
				v.linkright(L.sent = new SkipNode(L, Node.INF));
				L.getRoot().linkdown(oldr);
				L.sent.linkdown(olds);
				L.reposition();
				oldv = v;
				v = new SkipNode(L, v.key, v.tox, -10);
				++i;
				++L.height;
			}
			mysuspend();
		} while (R.nextInt(2) == 1);

		addStep("skipend");
		mysuspend();

		addStep("done");
		L.getV().setColor(NodeColor.NORMAL);
		L.setV(null);
	}
}
