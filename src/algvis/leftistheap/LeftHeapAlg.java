package algvis.leftistheap;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.VisPanel;

public class LeftHeapAlg extends Algorithm {
	LeftHeap H;

	// LeftHeapNodeNode v;

	public LeftHeapAlg(VisPanel M) {
		super(M);
	}

	public LeftHeapAlg(LeftHeap H) {
		super(H.M);
		this.H = H;
	}

	public void meld(int i) {
		/*
		 * Bude to konecny cyklus a budeme prechadzat postupne vsetkymi vrcholmi
		 * root[i] haldy Vsetko to staci spravit raz. Kazdym prechodom cyklu sa
		 * o jeden level nizsie posunie jedna halda.
		 */

		BSTNode w = H.root[i];
		H.root[0].mark();
		w.mark();
		setText("leftmeldstart");
		mysuspend();
		while (true) { // asi by bolo lepsie kym H.root[0] != null (true){//
			H.root[0].mark();
			w.mark();
			// posuvame sa dole a nic nemenime
			if (!((LeftHeapNode) w).prec(H.root[0])) {// (w.key <
														// H.root[0].key){
				// dat podmienku, aby sa vypisoval spravny text pre min aj max
				// haldu.
				setText("leftmeldright", H.root[0].key, w.key);
				mysuspend();
			} else { // vymenime w a root[0], napojime root[0] ako praveho syna.
				setText("leftmeldswap", H.root[0].key, w.key);
				mysuspend();
				BSTNode tmp1 = w.parent;
				BSTNode tmp2 = H.root[0];

				H.root[0] = (LeftHeapNode) w;
				if (w.parent != null) {
					H.root[0].parent = null;
					tmp1.right = tmp2;
					tmp2.parent = tmp1;
					w = tmp2;
				} else {
					H.root[i] = (LeftHeapNode) tmp2;
					w = H.root[i];
				}
				H.reposition();
			}

			if (w.parent != null) {
				((LeftHeapNode) w.parent).rightline = true;
			}

			H.root[0].repos(H.root[0].tox, H.root[0].toy + H.yspan + 2
					* H.radius);
			H.root[0].unmark();
			w.unmark();

			if (w.right == null) { // jedine za tejto podmienky sa skonci cyklus
				// povieme, ze nie je co riesit a napojime v vpravo na w
				setText("leftmeldnoson", H.root[0].key, w.key);
				mysuspend();
				w.linkright(H.root[0]);
				H.root[0] = null;
				H.reposition();
				break;
			}

			// toto odstranenie hrany sa mi vizualne moc nepaci
			((LeftHeapNode) w).rightline = false;
			w = w.right;
		}
		// uprava rankov
		setText("leftrankupdate");
		mysuspend();

		LeftHeapNode tmp = (LeftHeapNode) w;

		while (tmp != null) {
			if ((tmp.left != null) && (tmp.right != null)) {
				tmp.rank = Math.min(((LeftHeapNode) tmp.left).rank,
						((LeftHeapNode) tmp.right).rank) + 1;
			} else {
				tmp.rank = 1;
			}

			tmp = ((LeftHeapNode) tmp.parent);
		}
		setText("leftrankstart"); // text ideme vymienat bratov
		mysuspend();
		// vymienanie s bratmi podla ranku
		tmp = (LeftHeapNode) w;

		while (tmp != null) {
			int l;
			if (tmp.left == null) {
				l = -47;
			} else {
				tmp.left.mark();
				l = ((LeftHeapNode) tmp.left).rank;
			}
			int r;
			if (tmp.right == null) {
				r = -47;
			} else {
				tmp.right.mark();
				r = ((LeftHeapNode) tmp.right).rank;
			}
			if (l < r) {
				setText("leftranksonch");
				mysuspend();
				tmp.swapChildren();
			} else {
				setText("leftranksonok");
				mysuspend();
			}

			H.reposition();

			if (tmp.left != null) {
				tmp.left.unmark();
			}
			if (tmp.right != null) {
				tmp.right.unmark();
			}
			tmp = ((LeftHeapNode) tmp.parent);
		}

		H.reposition();
		mysuspend();
		setText("done");

	}

}
