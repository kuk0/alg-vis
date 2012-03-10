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
package algvis.unionfind;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.Buttons;
import algvis.core.VisPanel;
import algvis.internationalization.IButton;
import algvis.internationalization.IComboBox;
import algvis.internationalization.ILabel;

public class UnionFindButtons extends Buttons {
	private static final long serialVersionUID = 2683381160819263717L;
	IButton makesetB, findB, unionB;
	IComboBox unionHeuristicCB, findHeuristicCB;

	public UnionFindButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		findB = new IButton(M.S.L, "button-uffind");
		findB.setMnemonic(KeyEvent.VK_F);
		findB.addActionListener(this);

		unionB = new IButton(M.S.L, "button-union");
		unionB.setMnemonic(KeyEvent.VK_U);
		unionB.addActionListener(this);

		P.add(findB);
		P.add(unionB);
	}

	@Override
	public void actionButtons2(JPanel P) {
		makesetB = new IButton(M.S.L, "button-makeset");
		makesetB.setMnemonic(KeyEvent.VK_A);
		makesetB.addActionListener(this);

		P.add(makesetB);
	}

	@Override
	public void initRandom() {
		random = new IButton(M.S.L, "button-random-unions");
		random.setMnemonic(KeyEvent.VK_R);
		random.addActionListener(this);
	}
	
	@Override
	public JPanel initThirdRow() {
		JPanel P = new JPanel();
		ILabel uhLabel = new ILabel(M.S.L, "uf-union-heuristic"), fhLabel = new ILabel(
				M.S.L, "uf-find-heuristic");
		String[] uh = { "uf-none", "uf-byrank" }, fh = { "uf-none",
				"uf-compresion", "uf-halving", "uf-splitting" };
		unionHeuristicCB = new IComboBox(M.S.L, uh);
		findHeuristicCB = new IComboBox(M.S.L, fh);
		unionHeuristicCB.addActionListener(this);
		findHeuristicCB.addActionListener(this);
		P.add(uhLabel);
		P.add(unionHeuristicCB);
		P.add(fhLabel);
		P.add(findHeuristicCB);
		return P;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		final UnionFind D = (UnionFind) this.D;
		if (evt.getSource() == makesetB) {
			int N = I.getInt(10, 1, 1000);
			D.makeSet(N);
		} else if (evt.getSource() == findB) {
			int count = D.count;
			final Vector<Integer> args = I.getVI(1, count + 1);
			if (D.firstSelected != null) {
				args.insertElementAt(D.firstSelected.key, 0);
				D.firstSelected = null;
			}
			if (D.secondSelected != null) {
				args.insertElementAt(D.secondSelected.key, 1);
				D.secondSelected.unmark();
				D.secondSelected = null;
			}
			if (args.size() == 0) {
				Random G = new Random(System.currentTimeMillis());
				args.add(G.nextInt(count) + 1);
			}
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					D.find(D.at(args.elementAt(0) - 1));
				}
			});
			t.start();
		} else if (evt.getSource() == unionB) {
			int count = D.count;
			final Vector<Integer> args = I.getVI(1, count + 1);
			if (D.firstSelected != null) {
				args.insertElementAt(D.firstSelected.key, 0);
				D.firstSelected = null;
			}
			if (D.secondSelected != null) {
				args.insertElementAt(D.secondSelected.key, 1);
				D.secondSelected = null;
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
				@Override
				public void run() {
					D.union(D.at(args.elementAt(0) - 1),
							D.at(args.elementAt(1) - 1));
				}
			});
			t.start();
		} else if (evt.getSource() == unionHeuristicCB) {
			int i = unionHeuristicCB.getSelectedIndex();
			if (i == 0 || i == 1)
				D.unionState = UnionFindUnion.UnionHeuristic.values()[i];
		} else if (evt.getSource() == findHeuristicCB) {
			int i = findHeuristicCB.getSelectedIndex();
			if (0 <= i && i < 4)
				D.pathCompression = UnionFindFind.FindHeuristic.values()[i];
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
