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

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;

abstract class SplayAlg extends Algorithm {
	final SplayTree T;
	protected final int K;

	SplayAlg(SplayTree T, int x) {
		super(T.panel, null);
		this.T = T;
		K = x;
	}

	SplayNode find(int K) throws InterruptedException {
		SplayNode w = (SplayNode) T.getRoot();
		SplayNode s = new SplayNode(T, this.K, ZDepth.ACTIONNODE);
		s.setColor(NodeColor.FIND);
		addToScene(s);
		s.goTo(w);
		addNote("splay-start", K);
		pause();
		while (true) {
			if (w.getKey() == K) {
				addNote("splay-found");
				break;
			} else if (w.getKey() < K) { // right
				if (w.getRight() == null) {
					addNote("splay-lower", K, w.getKey());
					break;
				}
				w = w.getRight();
				addStep("bstfindright", K, w.getKey());
			} else { // left
				if (w.getLeft() == null) {
					addNote("splay-higher", K, w.getKey());
					break;
				}
				w = w.getLeft();
				addStep("bstfindleft", K, w.getKey());
			}
			s.goTo(w);
			pause();
		}
		w.setColor(NodeColor.FIND);
		removeFromScene(s);
		pause();
		return w;
	}

	void splay(SplayNode w) throws InterruptedException {
		while (!w.isRoot()) {
			T.setW1(w);
			T.setW2(w.getParent());
			if (w.getParent().isRoot()) {
				addNote("splay-root");
				w.setArc(w.getParent());
				pause();
				w.noArc();
				T.rotate(w);
			} else {
				if (w.isLeft() == w.getParent().isLeft()) {
					if (w.isLeft()) {
						addNote("splay-zig-zig-left", w.getKey(), w.getParent().getKey());
					} else {
						addNote("splay-zig-zig-right", w.getKey(), w.getParent().getKey());
					}
					addStep("rotate", w.getParent().getKey());
					w.getParent().setArc(w.getParent().getParent());
					pause();
					w.getParent().noArc();
					T.setW2(w.getParent().getParent());
					T.rotate(w.getParent());
					w.setArc(w.getParent());
					addStep("rotate", w.getKey());
					pause();
					w.noArc();
					T.setW1(w.getParent());
					T.rotate(w);
					pause();
				} else {
					if (!w.isLeft()) {
						addNote("splay-zig-zag-left", w.getKey(), w.getParent().getKey());
					} else {
						addNote("splay-zig-zag-right", w.getKey(), w.getParent().getKey());
					}
					w.setArc(w.getParent());
					addStep("rotate", w.getKey());
					pause();
					w.noArc();
					T.rotate(w);
					w.setArc(w.getParent());
					addStep("rotate", w.getKey());
					pause();
					w.noArc();
					T.setW1(w.getParent());
					T.rotate(w);
					pause();
				}
			}
		}
		T.setW1(null);
		T.setW2(null);
		T.setRoot(w);
	}
}
