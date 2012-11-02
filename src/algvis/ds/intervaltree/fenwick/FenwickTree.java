package algvis.ds.intervaltree.fenwick;

import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;

import algvis.core.DataStructure;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

public class FenwickTree extends DataStructure {

	public static String adtName = "intervaltrees";
	public static String dsName = "fenwicktree";
	
	protected FenwickTree(VisPanel panel) {
		super(panel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return dsName;
	}

	@Override
	public String stats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(int x) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void move() throws ConcurrentModificationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected Rectangle2D getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

}
