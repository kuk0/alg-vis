/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováè, Katarína Kotrlová, Pavol Lukèa, Viktor Tomkoviè, Tatiana Tóthová
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
package algvis.pairingheap;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.MeldablePQ;
import algvis.core.TreeNode;
import algvis.core.View;

public class PairHeapNode extends TreeNode{

	public PairHeapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}
	
	public PairHeapNode(DataStructure D, int key) {
		super(D, key);
		//System.out.println(this.x + " " + this.y + " " + this.tox + " " + this.toy + " " + this.steps);
		bgKeyColor();
	}
	
	public PairHeapNode(PairHeapNode v) {
		this(v.D, v.key, v.x, v.y);
		this.setColor(v.getColor());
	}
	
	private void lowlight() {
		bgColor(new Color(200, 200 - key / 10, 0));
	}

	private void highlight() {
		bgKeyColor();
	}

	public void lowlightTree() {
		lowlight();
		
		if (getRight() != null) {
			getRight().lowlightTree();
		}
		
		if (getChild() != null) {
			getChild().lowlightTree();
		}
	}

	public void highlightTree() {
		highlight();
		
		if (getRight() != null) {
			getRight().highlightTree();
		}

		if (getChild() != null) {
			getChild().highlightTree();
		}
	}
	
	@Override
	public void drawTree(View v) {
		super.drawTree(v);
	}
	
	@Override
	public PairHeapNode getRight(){
		return (PairHeapNode) super.getRight();
	}
	
	@Override
	public PairHeapNode getParent(){
		return (PairHeapNode) super.getParent();
	}
	
	@Override
	public PairHeapNode getChild(){
		return (PairHeapNode) super.getChild();
	}
	
	public int numChildren(){
		PairHeapNode w = this.getChild();
		int num = 0;
		while (w != null) {
			num++;
			w = w.getRight();
		} 
		return num;
	}
	
	public PairHeapNode findMaxSon(){
		PairHeapNode w = this.getChild();
		PairHeapNode max = w;
		while (w != null) {
			if (w.prec(max)) {
				max = w;
			}
			w = w.getRight();
		} 
		return max;
	}
	
	public void addChildLeft (PairHeapNode w){

		if (w.getParent() != null){
			w.getParent().deleteChild(w);
			w.setParent(null);
		}
		
		if (getChild() == null){
			addChild(w);			
		} else {
			PairHeapNode tmp = this.getChild();
			tmp.setParent(null);
			setChild(null);
			addChild(w);
			addChild(tmp);
		}
	}
	
	public boolean prec(PairHeapNode v) {
		if (((MeldablePQ) D).minHeap) {
			return this.key < v.key;
		} else {
			return this.key > v.key;
		}

	}
	
	@Override
	public void drawEdges(View v) {
			if (thread) {
				v.setColor(Color.red);
				if (getChild() != null) {
					if ((state != INVISIBLE) && (getChild().state != INVISIBLE)){
						v.drawLine(x, y, getChild().x, getChild().y);
					}
				} else
					System.out.println("child: " + getChild());
				v.setColor(Color.black);
			} else {
				TreeNode w = getChild();
				while (w != null) {
					if ((state != INVISIBLE) && (w.state != INVISIBLE)) {
						v.setColor(Color.black);
						v.drawLine(x, y, w.x, w.y);
					}
					w.drawEdges(v);
					w = w.getRight();
				}
			}
	}

	/**
	 * Precedes or equals (see prec).
	 */
	public boolean preceq(PairHeapNode v) {
		if (((MeldablePQ) D).minHeap) {
			return this.key <= v.key;
		} else {
			return this.key >= v.key;
		}
	}
}
