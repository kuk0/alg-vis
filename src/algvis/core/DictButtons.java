package algvis.core;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.internationalization.IButton;

/**
 * The Class DictButtons.
 * All dictionary data structures need buttons "Insert", "Find", and "Delete".
 */
public class DictButtons extends Buttons {
	private static final long serialVersionUID = 8331529914377645715L;
	IButton insertB, findB, deleteB;

	public DictButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton(M.L, "button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		findB = new IButton(M.L, "find");
		findB.setMnemonic(KeyEvent.VK_F);
		findB.addActionListener(this);

		deleteB = new IButton(M.L, "button-delete");
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
			Thread t = new Thread(new Runnable() {
				public void run() {
					for (int x : args) {
						((Dictionary) D).insert(x);
					}
				}
			});
			t.start();
		} else if (evt.getSource() == findB) {
			final Vector<Integer> args = I.getVI();
			if (args.size() > 0) {
				Thread t = new Thread(new Runnable() {
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
	public void enableNext() {
		super.enableNext();
		insertB.setEnabled(false);
		findB.setEnabled(false);
		deleteB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		insertB.setEnabled(true);
		findB.setEnabled(true);
		deleteB.setEnabled(true);
	}

	@Override
	public void refresh() {
		super.refresh();
		insertB.refresh();
		findB.refresh();
		deleteB.refresh();
	}
}
