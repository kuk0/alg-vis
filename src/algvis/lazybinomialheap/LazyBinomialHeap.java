package algvis.lazybinomialheap;

import algvis.binomialheap.BinHeapNode;
import algvis.binomialheap.BinomialHeap;
import algvis.core.MeldablePQButtons;
import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.View;
import algvis.core.VisPanel;

public class LazyBinomialHeap extends BinomialHeap {
	public static String dsName = "lazybinheap";
	BinHeapNode[] cleanup;
	public static int arrayheight = 2 * minsepy;

	@Override
	public String getName() {
		return "lazybinheap";
	}

	public LazyBinomialHeap(VisPanel M) {
		super(M);
		reposition();
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
	public void draw(View V) {
		super.draw(V);
		if (cleanup != null && root[active] != null) {
			int x = root[active].x, y = -arrayheight;
			for (int i = 0; i < cleanup.length; ++i) {
				V.drawSquare(x, y, Node.radius);
				V.drawStringTop("" + i, x, y - Node.radius + 1, 9);
				if (cleanup[i] == null) {
					V.drawLine(x-Node.radius, y+Node.radius, x+Node.radius, y-Node.radius);
				} else {
					V.drawArrow(x, y, cleanup[i].x, cleanup[i].y - minsepy + Node.radius);
				}
				x += 2 * Node.radius;
			}
		}
	}

	@Override
	public void reposition() {
		super.reposition();
		M.screen.V.miny = -arrayheight - 50;
	}
}
