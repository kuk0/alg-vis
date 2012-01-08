package algvis.leftistheap;

import algvis.core.MeldablePQ;
import algvis.core.MeldablePQButtons;
import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;

//robene podla BinomialHeap
public class LeftHeap extends MeldablePQ {
	public static String dsName = "leftheap";
	int n = 0; // pocet vrcholov
	LeftHeapNode root[] = null, v = null, v2 = null;

	public LeftHeap(VisPanel M) {
		super(M);
		root = new LeftHeapNode[numHeaps + 1];

	}

	@Override
	public void insert(int x) {
		start(new LeftHeapInsert(this, active, x));
		/*
		 * active = 0; start(new LeftHeapInsert(this, 0, x+1)); active = 1;
		 */
		// n++;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	// pripadne to skontrolovat...
	protected Pair chooseHeaps(int i, int j) {
		if (i < 1 || i > numHeaps) {
			i = -1;
		}
		if (j < 1 || j > numHeaps) {
			j = -1;
		}
		if (i == -1 || j == -1) {
			j = i;
			i = active;
			if (i == j || j == -1) {
				j = (active == 1) ? 2 : 1;
				for (int k = 0; k <= numHeaps; ++k) {
					if (k != active && root[k] != null) {
						j = k;
						break;
					}
				}
			}
		}
		return new Pair(i, j);
	}

	@Override
	public void meld(int i, int j) {
		Pair p = chooseHeaps(i, j); // j pripojit k i
		i = p.first;
		j = p.second;
		((MeldablePQButtons) M.B).activeHeap.setValue(i);
		start(new LeftHeapMeld(this, i, j));
	}

	@Override
	public String stats() {

		if (root[active] == null) { // <<---- root
			return M.L.getString("size") + ": 0;   " + M.L.getString("height")
					+ ": 0 =  1.00\u00b7" + M.L.getString("opt") + ";   "
					+ M.L.getString("avedepth") + ": 0";
		} else {
			root[active].calcTree();
			return M.L.getString("size")
					+ ": "
					+ root[active].size
					+ ";   "
					+ M.L.getString("height")
					+ ": "
					+ root[active].height
					+ " = "
					+ StringUtils.format(
							root[active].height
									/ (Math.floor(lg(root[active].size)) + 1),
							2, 5)
					+ "\u00b7"
					+ M.L.getString("opt")
					+ ";   "
					+ M.L.getString("avedepth")
					+ ": "
					+ StringUtils.format(root[active].sumh
							/ (double) root[active].size, 2, -5);
		}

	}

	@Override
	public void clear() {
		for (int i = 1; i <= numHeaps; i++) {
			root[i] = null;
		}

		// root[active] = null;
		// reposition();

		setStats();

	}

	@Override
	public void decreaseKey(Node v, int delta) {

	}

	// tuto potom zmazat
	// opravit, aby sa posuvali, uplne prerobit.
	public void reposition() {
		// spravit nejako tak, ze najst najlavejsi a najpravejsi vrchol
		// a z toho sirku stromu a pridavat do plus

		int sumx = 0; // - this.radius - this.xspan;
		// je root[i].rightw + root[i].leftw sirka stromu?
		for (int i = 1; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].reposition();
				root[i].reboxTree();
				sumx += root[i].leftw;
				root[i].repos(sumx, root[i].y);
				sumx += root[i].rightw;

				if (i == active) {
					if (root[0] != null) {
						root[0].reposition();
						root[0].reboxTree();
						sumx += root[0].leftw;
						root[0].repos(sumx, root[0].y);
						sumx += root[0].rightw;
					}
				}

			}
		}
		M.S.V.setBounds(0, 0, sumx, y2);
	}

	@Override
	public void draw(View V) {
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].moveTree();
				root[i].drawTree(V);
			}
		}
		if (v != null) {
			v.moveTree();
			v.drawTree(V);
		}
		if (v2 != null) {
			v2.moveTree();
			v2.drawTree(V);
		}
	}

	// @Override
	public void highlight(int i) {
		active = i;
		if (root[active] != null) {
			root[active].highlightTree();
		}
	}

	// @Override
	public void lowlight() {
		if (root[active] != null) {
			root[active].lowlightTree();
		}
	}

}
