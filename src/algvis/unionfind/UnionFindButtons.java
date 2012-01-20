package algvis.unionfind;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.Buttons;
import algvis.core.VisPanel;
import algvis.internationalization.IButton;

public class UnionFindButtons extends Buttons {
	private static final long serialVersionUID = 2683381160819263717L;
	IButton makesetB, findB, unionB;

	public UnionFindButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		makesetB = new IButton(M.S.L, "button-makeset");
		makesetB.setMnemonic(KeyEvent.VK_M);
		makesetB.addActionListener(this);

		findB = new IButton(M.S.L, "button-uffind");
		findB.setMnemonic(KeyEvent.VK_F);
		findB.addActionListener(this);

		unionB = new IButton(M.S.L, "button-union");
		unionB.setMnemonic(KeyEvent.VK_U);
		unionB.addActionListener(this);

		P.add(makesetB);
		P.add(findB);
		P.add(unionB);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == makesetB) {
			final Vector<Integer> args = I.getVI();
			if (args.size() == 0) {
				args.add(1);
			}
			// is this thread necessary?
			Thread t = new Thread(new Runnable() {
				public void run() {
					for (int x : args) {
						for (int j = 0; j < x; j++) {
							((UnionFind) D).makeSet();
						}
					}
				}
			});
			t.start();
		} else if (evt.getSource() == findB) {
			int count = ((UnionFind) D).count;
			final Vector<Integer> args = I.getVI(1, count + 1);
			if (((UnionFind) D).firstSelected != null) {
				args.insertElementAt(((UnionFind) D).firstSelected.key, 0);
				((UnionFind) D).firstSelected = null;
			}
			if (((UnionFind) D).secondSelected != null) {
				args.insertElementAt(((UnionFind) D).secondSelected.key, 1);
				((UnionFind) D).secondSelected.unmark();
				((UnionFind) D).secondSelected = null;
			}
			if (args.size() == 0) {
				Random G = new Random(System.currentTimeMillis());
				args.add(G.nextInt(count) + 1);
			}
			Thread t = new Thread(new Runnable() {
				public void run() {
					((UnionFind) D).find(((UnionFind) D).at(args.elementAt(0) - 1));
				}
			});
			t.start();
		} else if (evt.getSource() == unionB) {
			int count = ((UnionFind) D).count;
			final Vector<Integer> args = I.getVI(1, count + 1);
			if (((UnionFind) D).firstSelected != null) {
				args.insertElementAt(((UnionFind) D).firstSelected.key, 0);
				((UnionFind) D).firstSelected = null;
			}
			if (((UnionFind) D).secondSelected != null) {
				args.insertElementAt(((UnionFind) D).secondSelected.key, 1);
				((UnionFind) D).secondSelected = null;
			}
			Random G = new Random(System.currentTimeMillis());
			switch (args.size()) {
			case 0:
				args.add(G.nextInt(count) + 1);
			case 1:
				int i;
				int ii = args.elementAt(0);
				do {
					i = G.nextInt(count) + 1;
				} while (i == ii);
				args.add(i);
			}
			// is this thread necessary?
			Thread t = new Thread(new Runnable() {
				public void run() {
					((UnionFind) D).union(
							((UnionFind) D).at(args.elementAt(0) - 1),
							((UnionFind) D).at(args.elementAt(1) - 1));
				}
			});
			t.start();
		}
	}

	@Override
	public void enableNext() {
		super.enableNext();
		makesetB.setEnabled(false);
		findB.setEnabled(false);
		unionB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		makesetB.setEnabled(true);
		findB.setEnabled(true);
		unionB.setEnabled(true);
	}

}
