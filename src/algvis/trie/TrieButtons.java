package algvis.trie;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.Buttons;
import algvis.core.VisPanel;
import algvis.internationalization.IButton;

public class TrieButtons extends Buttons {
	private static final long serialVersionUID = -368670840648549217L;
	IButton insertB, findB, deleteB;

	public TrieButtons(VisPanel M) {
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
			final Vector<String> args = I.getVS();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (String s : args) {
						((Trie) D).insert(s);
					}
				}
			});
			t.start();
		} else if (evt.getSource() == findB) {
			final Vector<String> args = I.getVS();
			if (args.size() > 0) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						for (String s : args) {
							((Trie) D).find(s);
						}
					}
				});
				t.start();
			}
		} else if (evt.getSource() == deleteB) {
			final Vector<String> args = I.getVS();
			if (args.size() > 0) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						for (String s : args) {
							((Trie) D).delete(s);
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
