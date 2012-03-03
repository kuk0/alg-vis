package algvis.core;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.bst.BST;
import algvis.internationalization.IButton;

/**
 * The Class DictButtons. All dictionary data structures need buttons "Insert",
 * "Find", and "Delete".
 */
public class DictButtons extends Buttons {
	private static final long serialVersionUID = 8331529914377645715L;
	IButton insertB, findB, deleteB;

	public DictButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton(M.S.L, "button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		findB = new IButton(M.S.L, "button-find");
		findB.setMnemonic(KeyEvent.VK_F);
		findB.addActionListener(this);

		deleteB = new IButton(M.S.L, "button-delete");
		deleteB.setMnemonic(KeyEvent.VK_D);
		deleteB.addActionListener(this);

		P.add(insertB);
		P.add(findB);
		P.add(deleteB);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == insertB) {
			final Vector<Integer> args = I.getNonEmptyVI();
			D.scenario.traverser.startNew(new Runnable() {
				@Override
				public void run() {
					for (int x : args) {
						if (D instanceof BST) {
							D.scenario.newAlgorithm();
							D.scenario.newStep();
						}
						((Dictionary) D).insert(x);
					}
				}
			}, false);
			if (args.size() == 1) {
				D.scenario.previous(false, false);
				D.scenario.next(true, true);
			}
			D.scenario.traverser.join();
			D.setStats();
			M.C.update();
			update();
		} else if (evt.getSource() == findB) {
			final Vector<Integer> args = I.getVI();
			if (args.size() > 0) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						for (int x : args) {
							((Dictionary) D).find(x);
						}
					}
				});
				t.start();
			}
		} else if (evt.getSource() == deleteB) {
			final Vector<Integer> args = I.getVI();
			if (args.size() > 0) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						for (int x : args) {
							((Dictionary) D).delete(x);
						}
					}
				});
				t.start();
			}
		}
	}

	@Override
	public void enableAll() {
		super.enableAll();
		insertB.setEnabled(true);
		findB.setEnabled(true);
		deleteB.setEnabled(true);
	}

	@Override
	public void disableAll() {
		super.disableAll();
		insertB.setEnabled(false);
		findB.setEnabled(false);
		deleteB.setEnabled(false);
	}
}
