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
package algvis.ds.priorityqueues;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.internationalization.IButton;
import algvis.internationalization.IRadioButton;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

/**
 * The Class PQButtons. All priority queues need buttons "Insert",
 * "Delete max/min", and "Increase/Decrease key". Furthermore, the user may
 * choose whether he wants a "min" or "max" version of the priority queue.
 */
public class PQButtons extends Buttons {
    private static final long serialVersionUID = 5632185496171660196L;
    private IButton insertB;
    private IButton deleteB;
    protected IButton decrKeyB;
    protected IRadioButton minB;
    protected IRadioButton maxB;
    private ButtonGroup minMaxGroup;

    private boolean lastMinHeap = ((PriorityQueue) D).minHeap;

    public PQButtons(VisPanel M) {
        super(M);
    }

    @Override
    public void actionButtons(JPanel P) {
        insertB = new IButton("button-insert");
        insertB.setMnemonic(KeyEvent.VK_I);
        insertB.addActionListener(this);

        deleteB = new IButton("button-deletemax");
        deleteB.setMnemonic(KeyEvent.VK_D);
        deleteB.addActionListener(this);

        if (((PriorityQueue) D).minHeap) {
            decrKeyB = new IButton("button-decreasekey");
        } else {
            decrKeyB = new IButton("button-increasekey");
        }
        decrKeyB.setMnemonic(KeyEvent.VK_K);
        decrKeyB.addActionListener(this);

        P.add(insertB);
        P.add(deleteB);
        P.add(decrKeyB);
    }

    @Override
    public void otherButtons(JPanel P) {
        minB = new IRadioButton("min");
        minB.setSelected(false);
        minB.addActionListener(this);
        maxB = new IRadioButton("max");
        maxB.setSelected(true);
        maxB.addActionListener(this);
        minMaxGroup = new ButtonGroup();
        minMaxGroup.add(minB);
        minMaxGroup.add(maxB);
        P.add(minB);
        P.add(maxB);
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
            if (panel.pauses && !args.isEmpty()){
                panel.history.rewind();
            }
        } else if (evt.getSource() == deleteB) {
            panel.history.saveEditId();
            ((PriorityQueue) D).delete();
            if (panel.pauses){
                panel.history.rewind();
            }
        } else if (evt.getSource() == decrKeyB) {
            final int delta = Math.abs(I.getInt(1));
            final Node w = ((PriorityQueue) D).chosen;
            if (w != null) {
                // TODO vypisat, ze ziadny vrchol nie je vybraty
                // nesedi hlaska "using the default value 1"
                panel.history.saveEditId();
                ((PriorityQueue) D).decreaseKey(w, delta);
                if (panel.pauses){
                    panel.history.rewind();
                }
            }
        } else if (evt.getSource() == minB && !((PriorityQueue) D).minHeap) {
            D.start(new Algorithm(panel) {
                @Override
                public void runAlgorithm() {
                    D.clear();
                    ((PriorityQueue) D).minHeap = true;
                }
            });
        } else if (evt.getSource() == maxB && ((PriorityQueue) D).minHeap) {
            D.start(new Algorithm(panel) {
                @Override
                public void runAlgorithm() {
                    D.clear();
                    ((PriorityQueue) D).minHeap = false;
                }
            });
        }
    }

    @Override
    public void setOtherEnabled(boolean enabled) {
        super.setOtherEnabled(enabled);
        insertB.setEnabled(enabled);
        deleteB.setEnabled(enabled);
        decrKeyB.setEnabled(enabled);
        minB.setEnabled(enabled);
        maxB.setEnabled(enabled);
    }

    @Override
    public void refresh() {
        super.refresh();
        if (lastMinHeap != ((PriorityQueue) D).minHeap) {
            lastMinHeap = ((PriorityQueue) D).minHeap;
            if (lastMinHeap) {
                minB.setSelected(true);
                maxB.setSelected(false);
                deleteB.setT("button-deletemin");
                decrKeyB.setT("button-decreasekey");
            } else {
                deleteB.setT("button-deletemax");
                decrKeyB.setT("button-increasekey");
                minB.setSelected(false);
                maxB.setSelected(true);
            }
        }
    }
}
