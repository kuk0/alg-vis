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

package algvis.ds.intervaltree;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import algvis.core.Algorithm;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ds.intervaltree.IntervalTrees.mimasuType;
import algvis.internationalization.IButton;
import algvis.internationalization.IRadioButton;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

public class IntervalButtons extends Buttons {

    private static final long serialVersionUID = 6383200811481633404L;
    private IButton insertB;
    private IButton findsumB;
    private IButton changeKeyB;
    private IRadioButton minB;
    private IRadioButton maxB;
    private IRadioButton sumB;
    private ButtonGroup minMaxSumGroup;

    private mimasuType lastMinTree = ((IntervalTrees) D).minTree;

    public IntervalButtons(VisPanel M) {
        super(M);
    }

    @Override
    public void actionButtons(JPanel P) {
        insertB = new IButton("button-insert");
        insertB.setMnemonic(KeyEvent.VK_I);
        insertB.addActionListener(this);

        /*
         * deleteB = new IButton("button-deletemax");
         * deleteB.setMnemonic(KeyEvent.VK_D); deleteB.addActionListener(this);
         */

        if (((IntervalTrees) D).minTree == mimasuType.MIN) {
            findsumB = new IButton("button-findmin");
        } else if (((IntervalTrees) D).minTree == mimasuType.MAX) {
            findsumB = new IButton("button-findmax");
        } else {
            findsumB = new IButton("button-findsum");
        }
        findsumB.setMnemonic(KeyEvent.VK_I);
        findsumB.addActionListener(this);

        changeKeyB = new IButton("button-changekey");
        changeKeyB.setMnemonic(KeyEvent.VK_K);
        changeKeyB.addActionListener(this);

        P.add(insertB);
        // P.add(deleteB);
        P.add(findsumB);
        P.add(changeKeyB);

    }

    @Override
    public void otherButtons(JPanel P) {
        minB = new IRadioButton("min");
        minB.setSelected(false);
        minB.addActionListener(this);
        maxB = new IRadioButton("max");
        maxB.setSelected(true);
        maxB.addActionListener(this);
        sumB = new IRadioButton("sum");
        sumB.setSelected(false);
        sumB.addActionListener(this);
        minMaxSumGroup = new ButtonGroup();
        minMaxSumGroup.add(minB);
        minMaxSumGroup.add(maxB);
        minMaxSumGroup.add(sumB);
        P.add(minB);
        P.add(maxB);
        P.add(sumB);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        if (evt.getSource() == insertB) {
            final Vector<Integer> args = I.getNonEmptyVI();
            panel.history.saveEditId();
            for (final int x : args) {
                D.insert(x);
            }
            if (panel.pauses) {
                panel.history.rewind();
            }
        } else if (evt.getSource() == findsumB) {
            final Vector<Integer> args = I.getVI();
            panel.history.saveEditId();
            if (args.size() > 1) {
                ((IntervalTrees) D).ofinterval(args.elementAt(0),
                    args.elementAt(1));
            } else {
                ((IntervalTrees) D).ofinterval(1, ((IntervalTree) D).numLeafs);
            }
            if (panel.pauses) {
                panel.history.rewind();
            }
        } else if (evt.getSource() == changeKeyB) {
            final int delta = Math.abs(I.getInt(1));
            final BSTNode w = ((BSTNode) ((IntervalTrees) D).chosen);
            panel.history.saveEditId();
            ((IntervalTrees) D).changeKey(w, delta);
            if (panel.pauses && w != null) {
                panel.history.rewind();
            }
        } else if (evt.getSource() == minB
            && ((IntervalTrees) D).minTree != mimasuType.MIN) {
            D.start(new Algorithm(panel) {
                @Override
                public void runAlgorithm() {
                    D.clear();
                    ((IntervalTrees) D).minTree = mimasuType.MIN;
                }
            });
        } else if (evt.getSource() == maxB
            && ((IntervalTrees) D).minTree != mimasuType.MAX) {
            D.start(new Algorithm(panel) {
                @Override
                public void runAlgorithm() {
                    D.clear();
                    ((IntervalTrees) D).minTree = mimasuType.MAX;
                }
            });
        } else if (evt.getSource() == sumB
            && ((IntervalTrees) D).minTree != mimasuType.SUM) {
            D.start(new Algorithm(panel) {
                @Override
                public void runAlgorithm() {
                    D.clear();
                    ((IntervalTrees) D).minTree = mimasuType.SUM;
                }
            });
        }
    }

    @Override
    public void setOtherEnabled(boolean enabled) {
        super.setOtherEnabled(enabled);
        insertB.setEnabled(enabled);
        findsumB.setEnabled(enabled);
        // deleteB.setEnabled(enabled);
        changeKeyB.setEnabled(enabled);
    }

    @Override
    public void refresh() {
        super.refresh();
        if (lastMinTree != ((IntervalTrees) D).minTree) {
            lastMinTree = ((IntervalTrees) D).minTree;
            if (lastMinTree == mimasuType.MIN) {
                findsumB.setT("button-findmin");
            } else if (lastMinTree == mimasuType.MAX) {
                findsumB.setT("button-findmax");
            } else if (lastMinTree == mimasuType.SUM) {
                findsumB.setT("button-findsum");
            }
        }
    }
}
