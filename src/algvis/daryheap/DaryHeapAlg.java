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
package algvis.daryheap;

import algvis.core.Algorithm;
import algvis.core.Node;

class DaryHeapAlg extends Algorithm {
	final DaryHeap H;
	DaryHeapNode v;

	DaryHeapAlg(DaryHeap H) {
		super(panel, d);
		this.H = H;
	}

	void bubbleup(DaryHeapNode v) {

		DaryHeapNode w = v.getParent();
		while (w != null && v.prec(w)) {
			H.v = new DaryHeapNode(v);
			H.v.mark();
			H.v2 = new DaryHeapNode(w);
			v.setKey(Node.NOKEY);
			w.setKey(Node.NOKEY);
			H.v.goTo(w);
			H.v2.goTo(v);
			pause();
			v.setKey(H.v2.getKey());
			w.setKey(H.v.getKey());
			v.setColor(H.v2.getColor());
			w.setColor(H.v.getColor());
			H.v.unmark();
			H.v = null;
			H.v2 = null;
			v = w;
			w = w.getParent();
		}

		addNote("done");
	}

	void bubbledown(DaryHeapNode v) {
		DaryHeapNode w;

		while (true) {
			w = null;
			if (v.isLeaf()) {
				break;
			}

			w = v.findMaxSon();
			if (v.prec(w)) {
				break;
			}
			H.v = new DaryHeapNode(v);
			H.v.mark();
			H.v2 = new DaryHeapNode(w);
			v.setKey(Node.NOKEY);
			w.setKey(Node.NOKEY);
			H.v.goTo(w);
			H.v2.goTo(v);
			pause();
			v.setKey(H.v2.getKey());
			w.setKey(H.v.getKey());
			v.setColor(H.v2.getColor());
			w.setColor(H.v.getColor());
			H.v.unmark();
			H.v = null;
			H.v2 = null;
			v = w;
		}

		addNote("done");
	}
}

