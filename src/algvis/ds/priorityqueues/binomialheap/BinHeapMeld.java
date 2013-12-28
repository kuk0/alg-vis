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
package algvis.ds.priorityqueues.binomialheap;

public class BinHeapMeld extends BinHeapAlg {
	private final int i;
	private final int j;

	public BinHeapMeld(BinomialHeap H, int i, int j) {
		super(H);
		this.i = i;
		this.j = j;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("meld", i, j);
		if (i == j) {
			return;
		}
		if (H.root[i] == null) {
			H.root[i] = H.root[j];
			H.min[i] = H.min[j];
			H.root[j] = H.min[j] = null;
			if (H.root[i] != null) {
				H.root[i].highlightTree();
			}
			H.reposition();
			addStep("binheap-top-empty");
			addNote("done");
			return;
		}
		if (H.root[j] == null) {
			addStep("binheap-bottom-empty");
			addNote("done");
			return;
		}
		H.root[0] = H.root[j];
		H.min[0] = H.min[j];
		H.root[j] = H.min[j] = null;
		H.reposition();

		meld(i);
	}
}
