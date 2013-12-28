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
package algvis.ds.dictionaries.scapegoattree;

import algvis.core.Algorithm;

abstract class GBAlg extends Algorithm {
	final GBTree T;
	GBNode v;
	final int K;

	GBAlg(GBTree T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	GBNode compr(GBNode r, int c) throws InterruptedException {
		GBNode w = r;
		final GBNode x = (c > 0) ? r.getRight() : r;
		w.mark();
		pause();
		for (int i = 0; i < c; ++i) {
			assert w != null;
			w.unmark();
			w = w.getRight();
			T.rotate(w);
			w = w.getRight();
			if (w != null) {
				w.mark();
			}
			pause();
		}
		if (w != null) {
			w.unmark();
		}
		return x;
	}

}
