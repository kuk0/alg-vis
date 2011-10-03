package algvis;

import java.util.Random;

public class SkipInsert extends SkipAlg {
	Random R;
	
	public SkipInsert(SkipList L, int x) { // Buttons B,
		super(L, x);
		v.bgColor(Node.INSERT);
		p = new SkipNode[L.height];
		setHeader("insertion");
		R = new Random();		
	}

	@Override
	public void run() {
		setText("skipinsertstart");
		SkipNode w = find();

		if (w.right.key == v.key) {
			setText("alreadythere");
			v.goDown();
			mysuspend();
			//System.out.println("dupl");
			return;
		}

		L.n++;
		setText("skipinsertafter");
		SkipNode z, oldv = null;
		int i = 0;
		do {
			if (i > 0) {
				setText("skippromote");
				L.e++;
			}
			if (i < L.height) {
				w = p[i++];
				z = w.right;
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
				SkipNode oldr = L.root, olds = L.sent;
				v.linkleft(L.root = new SkipNode(L, -Node.INF));
				v.linkright(L.sent = new SkipNode(L, Node.INF));
				L.root.linkdown(oldr);
				L.sent.linkdown(olds);
				L.reposition();
				oldv = v;
				v = new SkipNode(L, v.key, v.tox, -10);
				++i;
				++L.height;
			}
			mysuspend();
		} while (R.nextInt(2) == 1);

		setText("skipend");
		mysuspend();

		setText("done");
		L.v.bgColor(Node.NORMAL);
		L.v = null;
	}
}
