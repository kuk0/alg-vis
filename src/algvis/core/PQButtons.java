package algvis.core;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import algvis.bst.BSTNode;
import algvis.internationalization.IButton;
import algvis.internationalization.IRadioButton;

/**
 * The Class PQButtons. All priority queues need buttons "Insert",
 * "Delete max/min", and "Increase/Decrease key". Furthermore, the user may
 * choose whether he wants a "min" or "max" version of the priority queue.
 */
public class PQButtons extends Buttons {
	private static final long serialVersionUID = 5632185496171660196L;
	IButton insertB, deleteB, decrKeyB;
	IRadioButton minB, maxB;
	ButtonGroup minMaxGroup;

	public PQButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton(M.S.L, "button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		deleteB = new IButton(M.S.L, "button-deletemax");
		deleteB.setMnemonic(KeyEvent.VK_D);
		deleteB.addActionListener(this);

		if (((PriorityQueue) D).minHeap) {
			decrKeyB = new IButton(M.S.L, "button-decreasekey");
		} else {
			decrKeyB = new IButton(M.S.L, "button-increasekey");
		}
		decrKeyB.setMnemonic(KeyEvent.VK_K);
		decrKeyB.addActionListener(this);

		P.add(insertB);
		P.add(deleteB);
		P.add(decrKeyB);
	}

	@Override
	public void otherButtons(JPanel P) {
		minB = new IRadioButton(M.S.L, "min");
		minB.setSelected(false);
		minB.addActionListener(this);
		maxB = new IRadioButton(M.S.L, "max");
		maxB.setSelected(true);
		maxB.addActionListener(this);
		minMaxGroup = new ButtonGroup();
		minMaxGroup.add(minB);
		minMaxGroup.add(maxB);
		P.add(minB);
		P.add(maxB);
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
						((PriorityQueue) D).insert(x);
					}
				}
			});
			t.start();
		} else if (evt.getSource() == deleteB) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					((PriorityQueue) D).delete();
				}
			});
			t.start();
		} else if (evt.getSource() == decrKeyB) {
			final int delta = Math.abs(I.getInt(1));
			final BSTNode w = ((BSTNode) ((PriorityQueue) D).chosen);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					((PriorityQueue) D).decreaseKey(w, delta);
				}
			});
			t.start();
		} else if (evt.getSource() == minB && !((PriorityQueue) D).minHeap) {
			D.clear();
			deleteB.setT("button-deletemin");
			decrKeyB.setT("button-decreasekey");
			((PriorityQueue) D).minHeap = true;
		} else if (evt.getSource() == maxB && ((PriorityQueue) D).minHeap) {
			D.clear();
			deleteB.setT("button-deletemax");
			decrKeyB.setT("button-increasekey");
			((PriorityQueue) D).minHeap = false;
		}
	}

	@Override
	public void enableNext() {
		super.enableNext();
		insertB.setEnabled(false);
		deleteB.setEnabled(false);
		decrKeyB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		insertB.setEnabled(true);
		deleteB.setEnabled(true);
		decrKeyB.setEnabled(true);
		next.setEnabled(false);
	}
}
