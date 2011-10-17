package algvis.core;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import algvis.binomialheap.BinomialHeap;
import algvis.internationalization.IButton;
import algvis.internationalization.ILabel;
import algvis.internationalization.IRadioButton;


public class MeldablePQButtons extends Buttons implements ChangeListener {
	private static final long serialVersionUID = 1242711038059609653L;
	IButton insertB, deleteB, decrKeyB, meldB;
	public JSpinner activeHeap;
	ILabel activeLabel;
	IRadioButton minB, maxB;
	ButtonGroup minMaxGroup;

	public MeldablePQButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton(M.a, "button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		deleteB = new IButton(M.a, "button-deletemax");
		deleteB.setMnemonic(KeyEvent.VK_D);
		deleteB.addActionListener(this);

		if (((MeldablePQ)D).minHeap) {
			decrKeyB = new IButton(M.a, "button-decreasekey");
		} else {
			decrKeyB = new IButton(M.a, "button-increasekey");
		}
		decrKeyB.setMnemonic(KeyEvent.VK_K);
		decrKeyB.addActionListener(this);

		meldB = new IButton(M.a, "button-meld");
		deleteB.setMnemonic(KeyEvent.VK_M);
		meldB.addActionListener(this);

		P.add(insertB);
		P.add(deleteB);
		P.add(decrKeyB);
		P.add(meldB);
	}

	@Override
	public void otherButtons(JPanel P) {
		activeHeap = new JSpinner(new SpinnerNumberModel(1, 1,
				MeldablePQ.numHeaps, 1));
		activeHeap.addChangeListener(this);
		activeLabel = new ILabel(M.a, "activeheap");
		minB = new IRadioButton(M.a, "min");
		minB.setSelected(false);
		minB.addActionListener(this);
		maxB = new IRadioButton(M.a, "max");
		maxB.setSelected(true);
		maxB.addActionListener(this);
		minMaxGroup = new ButtonGroup();
		minMaxGroup.add(minB);
		minMaxGroup.add(maxB);
		P.add(activeLabel);
		P.add(activeHeap);
		P.add(minB);
		P.add(maxB);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == insertB) {
			final Vector<Integer> args = I.getNonEmptyVI();
			Thread t = new Thread(new Runnable() {
				public void run() {
					for (int x : args) {
						((MeldablePQ) D).insert(x);
					}
				}
			});
			t.start();
		} else if (evt.getSource() == deleteB) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					((MeldablePQ) D).delete();
				}
			});
			t.start();
		} else if (evt.getSource() == decrKeyB) {
			final int delta = Math.abs(I.getInt(1));
			final Node w = ((MeldablePQ)D).chosen;
			Thread t = new Thread(new Runnable() {
				public void run() {
					((MeldablePQ) D).decreaseKey(w, delta);
				}
			});
			t.start();
		} else if (evt.getSource() == meldB) {
			Vector<Integer> args = I.getVI();
			args.add(-1);
			args.add(-1);
			final int i = args.get(0);
			final int j = args.get(1);
			Thread t = new Thread(new Runnable() {
				public void run() {
					((MeldablePQ) D).meld(i, j);
				}
			});
			t.start();
		} else if (evt.getSource() == minB && !((MeldablePQ) D).minHeap) {
			D.clear();
			deleteB.setT("button-deletemin");
			decrKeyB.setT("button-decreasekey");
			((MeldablePQ) D).minHeap = true;
		} else if (evt.getSource() == maxB && ((MeldablePQ) D).minHeap) {
			D.clear();
			deleteB.setT("button-deletemax");
			decrKeyB.setT("button-increasekey");
			((MeldablePQ) D).minHeap = false;
		}
	}

	@Override
	public void enableNext() {
		super.enableNext();
		insertB.setEnabled(false);
		deleteB.setEnabled(false);
		decrKeyB.setEnabled(false);
		meldB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		insertB.setEnabled(true);
		deleteB.setEnabled(true);
		decrKeyB.setEnabled(true);
		meldB.setEnabled(true);
		next.setEnabled(false);
	}

	@Override
	public void refresh() {
		super.refresh();
		insertB.refresh();
		deleteB.refresh();
		decrKeyB.refresh();
		meldB.refresh();
		activeLabel.refresh();
	}

	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == activeHeap) {
			BinomialHeap H = ((BinomialHeap) D);
			H.lowlight();
			H.highlight((Integer) activeHeap.getValue());
			if (H.chosen != null) H.chosen.unmark();
		}
	}
}
