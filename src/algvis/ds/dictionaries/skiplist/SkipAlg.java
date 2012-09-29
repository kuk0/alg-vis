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
package algvis.ds.dictionaries.skiplist;

import algvis.core.Algorithm;

abstract class SkipAlg extends Algorithm {
	final SkipList L;
	SkipNode v;
	SkipNode p[];
	final int K;

	SkipAlg(SkipList L, int x) {
		super(L.panel);
		this.L = L;
		K = x;
	}

	SkipNode find() throws InterruptedException {
		SkipNode w = L.getRoot();
		v.goToRoot();
		pause();

		for (int i = L.height - 1;; --i) {
			while (w.getRight().getKey() < K) {
				addStep("skipnext");
				w = w.getRight();
				v.goTo(w);
				pause();
			}
			addStep("skipdown");
			p[i] = w;
			if (w.getDown() == null) {
				break;
			}
			w = w.getDown();
			v.goTo(w);
			pause();
		}
		pause();
		return w;
	}
}
