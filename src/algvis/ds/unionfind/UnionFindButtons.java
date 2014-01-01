/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ds.unionfind;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.AlgorithmAdapter;
import algvis.core.MyRandom;
import algvis.internationalization.IButton;
import algvis.internationalization.IComboBox;
import algvis.internationalization.ILabel;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

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
        findB = new IButton("button-uffind");
        findB.setMnemonic(KeyEvent.VK_F);
        findB.addActionListener(this);

        unionB = new IButton("button-union");
        unionB.setMnemonic(KeyEvent.VK_U);
        unionB.addActionListener(this);

        P.add(findB);
        P.add(unionB);
    }

    @Override
    public void actionButtons2(JPanel P) {
        makesetB = new IButton("button-makeset");
        makesetB.setMnemonic(KeyEvent.VK_A);
        makesetB.addActionListener(this);

        P.add(makesetB);
    }

    @Override
    public void initRandom() {
        random = new IButton("button-random-unions");
        random.setMnemonic(KeyEvent.VK_R);
        random.addActionListener(this);
    }

    @Override
    public JPanel initThirdRow() {
        final JPanel P = new JPanel();
        final ILabel uhLabel = new ILabel("uf-union-heuristic"), fhLabel = new ILabel(
            "uf-find-heuristic");
        final String[] uh = {
            "uf-none", "uf-byrank"
        }, fh = {
            "uf-none", "uf-compresion", "uf-halving", "uf-splitting"
        };
        unionHeuristicCB = new IComboBox(uh);
        findHeuristicCB = new IComboBox(fh);
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
            if (panel.history.canRedo()) {
                panel.newAlgorithmPool();
            }
            D.start(new AlgorithmAdapter(panel) {
                @Override
                public void runAlgorithm() throws InterruptedException {
                    D.makeSet(I.getInt(10, 1, 1000));
                }
            });
        } else if (evt.getSource() == findB) {
            if (panel.history.canRedo()) {
                panel.newAlgorithmPool();
            }
            final int count = D.count;
            final Vector<Integer> args = I.getVI(1, count);
            if (D.firstSelected != null) {
                args.insertElementAt(D.firstSelected.getKey(), 0);
                D.firstSelected = null;
            }
            if (D.secondSelected != null) {
                args.insertElementAt(D.secondSelected.getKey(), 1);
                D.secondSelected.unmark();
                D.secondSelected = null;
            }
            if (args.size() == 0) {
                args.add(MyRandom.Int(count));
            }
            D.find(D.at(args.elementAt(0)));
        } else if (evt.getSource() == unionB) {
            if (panel.history.canRedo()) {
                panel.newAlgorithmPool();
            }
            final int count = D.count;
            final Vector<Integer> args = I.getVI(1, count);
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
            switch (args.size()) {
            case 0:
                args.add(MyRandom.Int(count));
            case 1:
                int i;
                final int ii = args.elementAt(0);
                do {
                    i = MyRandom.Int(count);
                } while (i == ii);
                args.add(i);
            }
            D.union(D.at(args.elementAt(0)), D.at(args.elementAt(1)));
        } else if (evt.getSource() == unionHeuristicCB) {
            final int i = unionHeuristicCB.getSelectedIndex();
            if (i == 0 || i == 1) {
                D.unionState = UnionFindUnion.UnionHeuristic.values()[i];
            }
        } else if (evt.getSource() == findHeuristicCB) {
            final int i = findHeuristicCB.getSelectedIndex();
            if (0 <= i && i < 4) {
                D.pathCompression = UnionFindFind.FindHeuristic.values()[i];
            }
        }
    }

    @Override
    public void setOtherEnabled(boolean enabled) {
        super.setOtherEnabled(enabled);
        makesetB.setEnabled(enabled);
        findB.setEnabled(enabled);
        unionB.setEnabled(enabled);
    }
}
