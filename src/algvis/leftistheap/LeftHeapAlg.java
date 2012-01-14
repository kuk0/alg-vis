package algvis.leftistheap;

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

		LeftHeapNode w = H.root[i];
		//LeftHeapNode w = H.root[i];
		H.root[0].mark();
		w.mark();
		setText("leftmeldstart");
		mysuspend();
		while (true) { // asi by bolo lepsie kym H.root[0] != null (true){//
			H.root[0].mark();
			w.mark();
			// posuvame sa dole a nic nemenime
			if (w.prec(H.root[0])) {// (w.key < H.root[0].key){
				if (!H.minHeap){
					setText("leftmeldrightg", w.key, H.root[0].key);
				}else{
					setText("leftmeldrightl", w.key, H.root[0].key);
				}
				mysuspend();
			} else { // vymenime w a root[0], napojime root[0] ako praveho syna.
				if (!H.minHeap){
					setText("leftmeldswapl", w.key, H.root[0].key);
				}else{
					setText("leftmeldswapg", w.key, H.root[0].key);
				}
				w.setDoubleArrow(H.root[0]);
				mysuspend();
				w.noDoubleArrow();
				LeftHeapNode tmp1 = w.getParent();
				LeftHeapNode tmp2 = H.root[0];

				H.root[0] = w;
				if (w.getParent() != null) {
					H.root[0].setParent(null);
					tmp1.setRight(tmp2);
					tmp2.setParent(tmp1);
					w = tmp2;
				} else {
					H.root[i] = tmp2;
					w = H.root[i];
				}
				H.reposition();
			}

			if (w.getParent() != null) {
				w.getParent().rightline = true;
			}

			H.root[0].repos(H.root[0].tox, H.root[0].toy + H.yspan + 2
					* H.radius);
			H.root[0].unmark();
			w.unmark();

			if (w.getRight() == null) { // jedine za tejto podmienky sa skonci cyklus
				// povieme, ze nie je co riesit a napojime v vpravo na w
				setText("leftmeldnoson", H.root[0].key, w.key);
				mysuspend();
				w.linkRight(H.root[0]);
				H.root[0] = null;
				H.reposition();
				break;
			}

			// toto odstranenie hrany sa mi vizualne moc nepaci
			w.rightline = false;
			w = w.getRight();
			mysuspend();
		}
		// uprava rankov
		setText("leftrankupdate");
		mysuspend();

		LeftHeapNode tmp = w;

		while (tmp != null) {
			if ((tmp.getLeft() != null) && (tmp.getRight() != null)) {
				tmp.rank = Math.min(tmp.getLeft().rank,
						tmp.getRight().rank) + 1;
			} else {
				tmp.rank = 1;
			}

			tmp =tmp.getParent();
		}
		setText("leftrankstart"); // text ideme vymienat bratov
		mysuspend();
		// vymienanie s bratmi podla ranku
		tmp = w;

		while (tmp != null) {
			int l;
			if (tmp.getLeft() == null) {
				l = -47;
			} else {
				tmp.getLeft().mark();
				l = tmp.getLeft().rank;
			}
			int r;
			if (tmp.getRight() == null) {
				r = -47;
			} else {
				tmp.getRight().mark();
				r = tmp.getRight().rank;
			}
			if (l < r) {
				//setText("leftranksonch"); //potom vymazat z Messages
				mysuspend();
				tmp.swapChildren();
			} else {
				//setText("leftranksonok");  //potom vymazat z Messages
				mysuspend();
			}

			H.reposition();

			if (tmp.getLeft() != null) {
				tmp.getLeft().unmark();
			}
			if (tmp.getRight() != null) {
				tmp.getRight().unmark();
			}
			tmp = tmp.getParent();
		}

		H.reposition();
		//mysuspend();
		setText("done");

	}

}
