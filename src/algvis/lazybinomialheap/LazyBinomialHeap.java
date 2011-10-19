package algvis.lazybinomialheap;

import java.awt.Graphics;

import algvis.binomialheap.BinHeapNode;
import algvis.binomialheap.BinomialHeap;
import algvis.core.MeldablePQButtons;
import algvis.core.Pair;
import algvis.core.View;
import algvis.core.VisPanel;


public class LazyBinomialHeap extends BinomialHeap {
	public static String dsName = "lazybinheap";
	BinHeapNode[] cleanup;

	public LazyBinomialHeap(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new LazyBinHeapInsert(this, active, x));
	}

	@Override
	public void delete() {
		start(new LazyBinHeapDelete(this, active));
	}

	@Override
	public void meld(int i, int j) {
		Pair p = chooseHeaps(i, j);
		i = p.first;
		j = p.second;
		((MeldablePQButtons) M.B).activeHeap.setValue(i);
		start(new LazyBinHeapMeld(this, i, j));
	}

	@Override
	public void draw(Graphics G, View V) {
		super.draw(G, V);
		if (cleanup != null && root[active] != null) {
			int x = root[active].x, y = -4*(radius+yspan);
			for (int i=0; i<cleanup.length; ++i) {
				V.drawSquare(G, x, y, radius);
				V.drawStringTop(G, ""+(1<<i), x, y-radius+1, 9);
				if (cleanup[i] != null) {
					V.drawArrow(G, x, y, cleanup[i].x, cleanup[i].y-radius-yspan);
				}
				x += 2 * radius;
			}	
		}
	}
}
