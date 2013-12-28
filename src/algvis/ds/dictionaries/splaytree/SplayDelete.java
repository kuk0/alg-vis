/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ds.dictionaries.splaytree;

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;

public class SplayDelete extends SplayAlg {
	private final int K;

	public SplayDelete(SplayTree T, int x) {
		super(T, x);
		K = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("delete", K);
		SplayNode s = new SplayNode(T, K, ZDepth.ACTIONNODE);
		s.setColor(NodeColor.DELETE);
		addToScene(s);
		if (T.getRoot() == null) {
			s.goToRoot();
			addStep("empty");
			pause();
			s.goDown();
			s.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
			removeFromScene(s);
			return;
		}
		s.goAboveRoot();

		SplayNode w = find(K);

		if (w.getKey() != s.getKey()) {
			addStep("notfound");
			s.setColor(NodeColor.NOTFOUND);
			s.goDown();
			removeFromScene(s);
			splay(w);
			w.setColor(NodeColor.NORMAL);
			return;
		}
		w.setColor(NodeColor.DELETE);
		removeFromScene(s);
		splay(w);

		pause();
		addToScene(w);
		w.goDown();
		removeFromScene(w);
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
			T.getRoot2().shiftTree(-75, 0);
			T.getRoot2().setParent(null);
			T.setRoot(w.getRight());
			T.getRoot().setParent(null);
			s = new SplayNode(T, -Node.INF, ZDepth.ACTIONNODE);
			s.setColor(NodeColor.FIND);
			addToScene(s);
			w = w.getRight();
			s.goTo(w);
			pause();
			while (w.getLeft() != null) {
				w = w.getLeft();
				s.goTo(w);
				pause();
			}
			w.setColor(NodeColor.FIND);
			removeFromScene(s);
			// splay TODO: shades
			// TODO: reuse splaying
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

		addNote("done");
	}
}
