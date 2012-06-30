package algvis.linkcuttree;

import java.util.ArrayList;

import algvis.core.DataStructure;
import algvis.gui.VisPanel;
import algvis.gui.view.ClickListener;
import algvis.gui.view.View;


public class LinkCutDS extends DataStructure implements ClickListener {
	public static String adtName = "lct";
	public static String dsName = "lct";
//	private ArrayList<ETtree> trees;
	private ArrayList<LinkCutDSNode> tree;
	int max;
	
	public LinkCutDSNode firstSelected = null;
	public LinkCutDSNode secondSelected = null;

	public LinkCutDS(VisPanel M) {
		super(M);
		max = 1;
//		trees = new ArrayList<ETtree>();
		tree = new ArrayList<LinkCutDSNode>();
		addElements(10);
	}
		
	public void addElements(int x) {
//		ETtree tmp;
		LinkCutDSNode node;
		for(int i=0; i<x; i++) {
/*			tmp = new ETtree(M);
			tmp.setRoot(new ETNode(tmp,max+i));
			trees.add(tmp);*/
			
			node = new LinkCutDSNode(this, max+i);
			tree.add(node);
		}
		max += x;
		reposition();
	}
	
	public void link(int x, int y) {
		int index = 0;
		LinkCutDSNode N = null, M;
		for(int i=0; i<tree.size(); i++) {
			if (tree.get(i).getKey() == x) {index = i;}
			M = tree.get(i).getNode(y);
			if (M != null) {N = M;}
		}
		start(new Link(this, tree.get(index),N));
		tree.remove(index);
		reposition();
	}
	
	public void cut(int x) {
		LinkCutDSNode N = null, M;
		for(int i=0; i<tree.size(); i++) {
			M = tree.get(i).getNode(x);
			if (M != null) {N = M;}
		}
		start(new Cut(this, N));
		tree.add(N);
		reposition();
	}

	@Override
	public void clear() {
		max = 1;
		tree = new ArrayList<LinkCutDSNode>();
//		trees = new ArrayList<ETtree>();
		addElements(10);
		setStats();
	}

	@Override
	public void draw(View V) {
		if (tree != null) {
			for (int i = 0; i < tree.size(); i++) {
			//	trees.get(i).draw(V);
				
				tree.get(i).moveTree();
				tree.get(i).drawTree(V);
			}
		}
/*		if (v != null) {
			if (isSelected(v) && (v.marked == false)) {
				// v.mark(); // TODO
			}
			if (!isSelected(v) && (v.marked != false)) {
				// v.unmark(); // TODO are these lines needed?
			}
			v.move();
			v.draw(V);
		}*/
	}

	@Override
	public String getName() {
		return "lct";
	}

	@Override
	public void insert(int x) {
	}

	@Override
	public String stats() {
		return "";
	}
	
	public void reposition() {
		if (tree != null) {
			int ey2 = -9999999;
			int ey1 = 9999999;
			for (int i = 0; i < tree.size(); i++) {
				y1 = y2 = 0;
				tree.get(i).reposition();
				if (y1 < ey1) {
					ey1 = y1;
				}
				if (y2 > ey2) {
					ey2 = y2;
				}
			}
			y1 = ey1;
			y2 = ey2;

			x1 = x2 = 0;
			int shift = -tree.get(0).leftw;
			x1 = shift;
			for (int i = 0; i < tree.size(); i++) {
				shift += tree.get(i).leftw;
				tree.get(i).shift(shift, 0);
				shift += tree.get(i).rightw;
			}
			x2 = shift;
			M.screen.V.setBounds(x1, y1, x2, y2);
		}

/*		if (trees != null) {
			int ey2 = -9999999;
			int ey1 = 9999999;
			for (int i = 0; i < trees.size(); i++) {
				y1 = y2 = 0;
				trees.get(i).reposition();
				if (y1 < ey1) {
					ey1 = y1;
				}
				if (y2 > ey2) {
					ey2 = y2;
				}
			}
			y1 = ey1;
			y2 = ey2;

			x1 = x2 = 0;
			int shift = -trees.get(0).getRoot().leftw;
			x1 = shift;
			for (int i = 0; i < trees.size(); i++) {
				shift += trees.get(i).getRoot().leftw;
				trees.get(i).getRoot().goTo(shift, 0);
				//trees.get(i).reposition();
				shift += trees.get(i).getRoot().rightw;
			}
			x2 = shift;
			M.screen.V.setBounds(x1, y1, x2, y2);
		}
*/
	}
	
	public boolean isSelected(LinkCutDSNode u) {
		if ((u == firstSelected) || (u == secondSelected))
			return true;
		else
			return false;
	}
	
	@Override
	public void mouseClicked(int x, int y) {
		LinkCutDSNode u = null;
		int i = 0;
		int j = tree.size();
		do {
			u = (LinkCutDSNode) tree.get(i).find(x, y);
			i++;
		} while ((u == null) && (i < j));
		if (u != null) {
			if (isSelected(u)) {
				scenario.enableAdding(false);
				u.unmark();
				scenario.enableAdding(true);
				if (u == secondSelected) {
					secondSelected = null;
				} else if (u == firstSelected) {
					firstSelected = secondSelected;
					secondSelected = null;
				}
			} else {
				scenario.enableAdding(false);
				u.mark();
				scenario.enableAdding(true);
				if (firstSelected == null) {
					firstSelected = u;
				} else if (secondSelected == null) {
					secondSelected = u;
				} else {
					scenario.enableAdding(false);
					firstSelected.unmark();
					scenario.enableAdding(true);
					firstSelected = secondSelected;
					secondSelected = u;
				}
			}
		}
	}


}
