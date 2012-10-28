/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.ds.priorityqueues.leftistheap;

import algvis.core.Algorithm;
import algvis.core.Node;

abstract class LeftHeapAlg extends Algorithm {
	final LeftHeap H;
	LeftHeapNode v;

	LeftHeapAlg(LeftHeap H) {
		super(H.panel);
		this.H = H;
	}

	void meld(int i) throws InterruptedException {
		LeftHeapNode w = H.root[i];
		H.root[0].mark();
		w.mark();
		addStep("leftmeldstart");
		pause();
		while (true) {
			H.root[0].mark();
			w.mark();
			if (w.prec(H.root[0])) {
				if (!H.minHeap) {
					addStep("leftmeldrightg", w.getKey(), H.root[0].getKey());
				} else {
					addStep("leftmeldrightl", w.getKey(), H.root[0].getKey());
				}
				pause();
			} else {
				if (!H.minHeap) {
					addStep("leftmeldswapl", w.getKey(), H.root[0].getKey());
				} else {
					addStep("leftmeldswapg", w.getKey(), H.root[0].getKey());
				}
				w.setDoubleArrow(H.root[0]);
				pause();
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
				w.getParent().dashedRightLine = false;
			}
			H.root[0].repos(H.root[0].tox, H.root[0].toy + LeftHeap.minsepy);// +
																				// 2*
																				// LeftHeapNode.RADIUS);
			H.root[0].unmark();
			w.unmark();

			if (w.getRight() == null) {
				addStep("leftmeldnoson", H.root[0].getKey(), w.getKey());
				pause();
				w.linkRight(H.root[0]);
				H.root[0] = null;
				H.reposition();
				break;
			}

			w.dashedRightLine = true;
			w = w.getRight();
			pause();
		}
		addNote("leftrankupdate");
		pause();

		LeftHeapNode tmp = w;

		while (tmp != null) {
			if ((tmp.getLeft() != null) && (tmp.getRight() != null)) {
				tmp.rank = Math.min(tmp.getLeft().rank, tmp.getRight().rank) + 1;
			} else {
				tmp.rank = 1;
			}

			tmp = tmp.getParent();
		}
		addNote("leftrankstart");
		pause();

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
			pause();
			if (l < r) {
				tmp.swapChildren();
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
		addNote("done");
	}

	void bubbleup(LeftHeapNode v) throws InterruptedException {
		if (H.minHeap) {
			addStep("minheapbubbleup");
		} else {
			addStep("maxheapbubbleup");
		}
		v.mark();
		pause();
		v.unmark();
		LeftHeapNode w = v.getParent();
		LeftHeapNode v1, v2;
		while (w != null && v.prec(w)) {
			v1 = new LeftHeapNode(v);
			v1.rank = -1;
			v1.mark();
			v2 = new LeftHeapNode(w);
			v2.rank = -1;
			addToScene(v1);
			addToScene(v2);
			v.setKey(Node.NOKEY);
			w.setKey(Node.NOKEY);
			v1.goTo(w);
			v2.goTo(v);
			pause();
			v.setKey(v2.getKey());
			w.setKey(v1.getKey());
			v.setColor(v2.getColor());
			w.setColor(v1.getColor());
			v1.unmark();
			removeFromScene(v1);
			removeFromScene(v2);
			v = w;
			w = w.getParent();
		}
		addNote("done");
	}

}
