package algvis.leftistheap;

import java.awt.Graphics;

import algvis.bst.BSTNode;
import algvis.core.MeldablePQ;
import algvis.core.Node;
import algvis.core.View;
import algvis.core.VisPanel;


public class LeftHeap extends MeldablePQ{
	public static String dsName = "leftheap";
	int n = 0;    //pocet vrcholov
	BSTNode root = null, v = null, v2 = null;
	
	
	
	public LeftHeap(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new LeftHeapInsert(this, x));
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meld(int i, int j) {
		// TODO Auto-generated method stub						
		
	}


	@Override
	public String stats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void draw(Graphics G, View V) {
		// TODO Auto-generated method stub
		if (root != null) {
			root.moveTree();
			root.drawTree(G, V);
		}
		if (v != null) {
			v.move();
			v.draw(G, V);
		}
		if (v2 != null) {
			v2.move();
			v2.draw(G, V);
		}
		
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		// TODO Auto-generated method stub
		
	}
	
	//tuto potom zmazat
	public void reposition() {
		if (root != null) {
			root.reposition();
			M.S.V.setBounds(x1, y1, x2, y2);
		}
	}	
	

}
