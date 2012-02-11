package algvis.scapegoattree;

import algvis.bst.BST;
import algvis.core.Layout;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;
import algvis.scenario.commands.gbnode.SetDelCommand;

public class GBTree extends BST {
	public static String dsName = "scapegoat";
	double alpha = 1.01;
	private int del = 0;

	@Override
	public String getName() {
		return "scapegoat";
	}

	public GBTree(VisPanel M) {
		super(M);
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		if (this.del != del) {
			if (scenario.isAddingEnabled()) {
				scenario.add(new SetDelCommand(this, del));
			}
			this.del = del;
		}
	}

	@Override
	public void insert(int x) {
		start(new GBInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new GBFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new GBDelete(this, x));
	}

	@Override
	public void clear() {
		super.clear();
		setDel(0);
	}

	@Override
	public void draw(View V) {
		if (getRoot() != null) {
			getRoot().moveTree();
			getRoot().drawTree(V);
		}
		if (getV() != null) {
			getV().move();
			getV().draw(V);
		}
	}

	@Override
	public String stats() {
		if (getRoot() == null) {
			return "#" + M.S.L.getString("nodes") + ": 0;   #"
					+ M.S.L.getString("deleted") + ": 0;   "
					+ M.S.L.getString("height") + ": 0 =  1.00\u00b7"
					+ M.S.L.getString("opt") + ";   "
					+ M.S.L.getString("avedepth") + ": 0";
		} else {
			getRoot().calcTree();
			return "#"
					+ M.S.L.getString("nodes")
					+ ": "
					+ getRoot().size
					+ ";   #"
					+ M.S.L.getString("deleted")
					+ ": "
					+ getDel()
					+ ";   "
					+ M.S.L.getString("height")
					+ ": "
					+ getRoot().height
					+ " = "
					+ StringUtils
							.format(getRoot().height
									/ (Math.floor(lg(getRoot().size - getDel())) + 1),
									2, 5)
					+ "\u00b7"
					+ M.S.L.getString("opt")
					+ ";   "
					+ M.S.L.getString("avedepth")
					+ ": "
					+ StringUtils.format(getRoot().sumh
							/ (double) getRoot().size, 2, -5);
		}
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
