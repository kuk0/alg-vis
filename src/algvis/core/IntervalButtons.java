package algvis.core;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import algvis.bst.BSTNode;
import algvis.internationalization.IButton;
import algvis.internationalization.IRadioButton;

public class IntervalButtons extends Buttons{

	private static final long serialVersionUID = 6383200811481633404L;
	IButton insertB, findsumB, decrKeyB;
	IRadioButton minB, maxB;
	ButtonGroup minMaxGroup;

	public IntervalButtons(VisPanel M) {
		super(M);
	}
	
	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton(M.S.L, "button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		/*
		deleteB = new IButton(M.S.L, "button-deletemax");
		deleteB.setMnemonic(KeyEvent.VK_D);
		deleteB.addActionListener(this);
		*/
		
		findsumB = new IButton(M.S.L, "button-findmax");
		findsumB.setMnemonic(KeyEvent.VK_I);
		findsumB.addActionListener(this);
		

		if (((IntervalTrees) D).minTree) {
			decrKeyB = new IButton(M.S.L, "button-decreasekey");
		} else {
			decrKeyB = new IButton(M.S.L, "button-increasekey");
		}
		decrKeyB.setMnemonic(KeyEvent.VK_K);
		decrKeyB.addActionListener(this);

		P.add(insertB);
		//P.add(deleteB);
		P.add(findsumB);
		P.add(decrKeyB);
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == insertB) {
			final Vector<Integer> args = I.getNonEmptyVI();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int x : args) {
						((IntervalTrees) D).insert(x);
					}
				}
			});
			t.start();
		} else if (evt.getSource() == findsumB) {
			final Vector<Integer> args = I.getVI();
			if (args.size() > 1) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						((IntervalTrees) D).ofinterval(args.elementAt(0), args.elementAt(1));
					}
				});
				t.start();
			}
		} else if (evt.getSource() == decrKeyB) {
			final int delta = Math.abs(I.getInt(1));
			final BSTNode w = ((BSTNode) ((IntervalTrees) D).chosen);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					((IntervalTrees) D).decreaseKey(w, delta);
				}
			});
			t.start();
		} else if (evt.getSource() == minB && !((IntervalTrees) D).minTree) {
			D.clear();
			//deleteB.setT("button-deletemin");
			decrKeyB.setT("button-decreasekey");
			((PriorityQueue) D).minHeap = true;
		} else if (evt.getSource() == maxB && ((IntervalTrees) D).minTree) {
			D.clear();
			//deleteB.setT("button-deletemax");
			decrKeyB.setT("button-increasekey");
			((PriorityQueue) D).minHeap = false;
		}
	}

	@Override
	public void enableNext() {
		super.enableNext();
		insertB.setEnabled(false);
		findsumB.setEnabled(false);
		//deleteB.setEnabled(false);
		decrKeyB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		insertB.setEnabled(true);
		findsumB.setEnabled(true);
		//deleteB.setEnabled(true);
		decrKeyB.setEnabled(true);
		next.setEnabled(false);
	}

}
