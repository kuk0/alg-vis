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

import algvis.gui.Buttons;
import algvis.gui.VisPanel;
import algvis.internationalization.IButton;
import algvis.internationalization.IComboBox;
import algvis.internationalization.ILabel;

public class UnionFindButtons extends Buttons {
	private static final long serialVersionUID = 2683381160819263717L;
	private IButton makesetB;
    private IButton findB;
    private IButton unionB;
	private IComboBox unionHeuristicCB;
    private IComboBox findHeuristicCB;

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
			final int N = I.getInt(10, 1, 1000);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					D.M.scenario.newAlgorithm();
					D.M.scenario.newStep();
					D.makeSet(N);
					M.B.update();
				}
			});
			t.start();
		} else if (evt.getSource() == findB) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					int count = D.count;
					final Vector<Integer> args = I.getVI(1, count);
					if (D.firstSelected != null) {
						args.insertElementAt(D.firstSelected.getKey(), 0);
						D.firstSelected = null;
					}
					if (D.secondSelected != null) {
						args.insertElementAt(D.secondSelected.getKey(), 1);
						D.M.scenario.enableAdding(false);
						D.secondSelected.unmark();
						D.M.scenario.enableAdding(true);
						D.secondSelected = null;
					}
					if (args.size() == 0) {
						Random G = new Random(System.currentTimeMillis());
						args.add(G.nextInt(count));
					}
					D.find(D.at(args.elementAt(0)));
				}
			});
			t.start();
		} else if (evt.getSource() == unionB) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					int count = D.count;
					final Vector<Integer> args = I.getVI(1, count);
					D.M.scenario.enableAdding(false);
					if (D.firstSelected != null) {
						args.insertElementAt(D.firstSelected.getKey(), 0);
						D.firstSelected.unmark();
						D.firstSelected = null;
					}
					if (D.secondSelected != null) {
						args.insertElementAt(D.secondSelected.getKey(), 1);
						D.secondSelected.unmark();
						D.secondSelected = null;
					}
					D.M.scenario.enableAdding(true);
					Random G = new Random(System.currentTimeMillis());
					switch (args.size()) {
					case 0:
						args.add(G.nextInt(count));
					case 1:
						int i;
						int ii = args.elementAt(0);
						do {
							i = G.nextInt(count);
						} while (i == ii);
						args.add(i);
					}
					D.union(D.at(args.elementAt(0)), D.at(args.elementAt(1)));
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
	public void disableAll() {
		super.disableAll();
		makesetB.setEnabled(false);
		findB.setEnabled(false);
		unionB.setEnabled(false);
	}

	@Override
	public void enableAll() {
		super.enableAll();
		makesetB.setEnabled(true);
		findB.setEnabled(true);
		unionB.setEnabled(true);
	}
}
