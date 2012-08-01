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
package algvis.splaytree;

import algvis.core.NodeColor;
import algvis.core.Node;

public class SplayDelete extends SplayAlg {
	public SplayDelete(SplayTree T, int x) {
		super(T, x);
		setHeader("delete", x);
	}

	@Override
	public void run() {
		if (T.getRoot() == null) {
			s.goToRoot();
			addStep("empty");
			pause();
			s.goDown();
			s.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
			return;
		}

		SplayNode w = find(K);
		splay(w);

		w.setColor(NodeColor.NORMAL);

		if (w.getKey() != s.getKey()) {
			addStep("notfound");
			s.setColor(NodeColor.NOTFOUND);
			s.goDown();
			return;
		}

		T.setV(w);
		T.getV().goDown();
		T.getV().setColor(NodeColor.DELETE);
		if (w.getLeft() == null) {
			addStep("splaydeleteright");
			T.setRoot(w.getRight());
			T.getRoot().setParent(null);
			T.reposition();
			pause();
		} else if (w.getRight() == null) {
			addStep("splaydeleteleft");
			T.setRoot(w.getLeft());
			T.getRoot().setParent(null);
			T.reposition();
			pause();
		} else {
			addStep("splaydelete");
			T.setRoot2(w.getLeft());
			T.getRoot2().setParent(null);
			T.setRoot(w.getRight());
			T.getRoot().setParent(null);
			T.setVV(s = new SplayNode(T, -Node.INF));
			s.setColor(NodeColor.FIND);
			w = w.getRight();
			s.goTo(w);
			pause();
			while (w.getLeft() != null) {
				w = w.getLeft();
				s.goTo(w);
				pause();
			}
			w.setColor(NodeColor.FIND);
			T.setVV(null);
			// splay
			while (!w.isRoot()) {
				if (w.getParent().isRoot()) {
					T.rotate2(w);
					// setText ("splayroot");
				} else {
					if (w.isLeft() == w.getParent().isLeft()) {
						/*
						 * if (w.isLeft()) setText ("splayzigzigleft"); else
						 * setText ("splayzigzigright");
						 */
						T.rotate2(w.getParent());
						pause();
						T.rotate2(w);
					} else {
						/*
						 * if (!w.isLeft()) setText ("splayzigzagleft"); else
						 * setText ("splayzigzagright");
						 */
						T.rotate2(w);
						pause();
						T.rotate2(w);
					}
				}
				pause();
			}
			addStep("splaydeletelink");
			T.setRoot(w);
			w.setColor(NodeColor.NORMAL);
			w.linkLeft(T.getRoot2());
			T.setRoot2(null);
			T.reposition();
			pause();
		}

		addStep("done");
		T.setVV(null);
	}
}
