/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
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
		insertB = new IButton(M.S.L, "button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		deleteB = new IButton(M.S.L, "button-deletemax");
		deleteB.setMnemonic(KeyEvent.VK_D);
		deleteB.addActionListener(this);

		if (((MeldablePQ) D).minHeap) {
			decrKeyB = new IButton(M.S.L, "button-decreasekey");
		} else {
			decrKeyB = new IButton(M.S.L, "button-increasekey");
		}
		decrKeyB.setMnemonic(KeyEvent.VK_K);
		decrKeyB.addActionListener(this);

		meldB = new IButton(M.S.L, "button-meld");
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
		activeLabel = new ILabel(M.S.L, "activeheap");
		minB = new IRadioButton(M.S.L, "min");
		minB.setSelected(false);
		minB.addActionListener(this);
		maxB = new IRadioButton(M.S.L, "max");
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
				@Override
				public void run() {
					for (int x : args) {
						((MeldablePQ) D).insert(x);
					}
				}
			});
			t.start();
		} else if (evt.getSource() == deleteB) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					((MeldablePQ) D).delete();
				}
			});
			t.start();
		} else if (evt.getSource() == decrKeyB) {
			final int delta = Math.abs(I.getInt(1));
			final Node w = ((MeldablePQ) D).chosen;
			Thread t = new Thread(new Runnable() {
				@Override
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
				@Override
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
		activeHeap.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		insertB.setEnabled(true);
		deleteB.setEnabled(true);
		decrKeyB.setEnabled(true);
		meldB.setEnabled(true);
		next.setEnabled(false);
		activeHeap.setEnabled(true);
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == activeHeap) {
			MeldablePQ H = ((MeldablePQ) D);
			H.lowlight();
			H.highlight((Integer) activeHeap.getValue());
			if (H.chosen != null)
				H.chosen.unmark();
		}
	}
}
