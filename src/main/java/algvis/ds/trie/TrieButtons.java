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
package algvis.ds.trie;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.internationalization.IButton;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

public class TrieButtons extends Buttons {
    private static final long serialVersionUID = -368670840648549217L;
    private IButton insertB;
    private IButton findB;
    private IButton deleteB;

    public TrieButtons(VisPanel M) {
        super(M);
    }

    @Override
    public void actionButtons(JPanel P) {
        insertB = new IButton("button-insert");
        insertB.setMnemonic(KeyEvent.VK_I);
        insertB.addActionListener(this);

        findB = new IButton("button-find");
        findB.setMnemonic(KeyEvent.VK_F);
        findB.addActionListener(this);

        deleteB = new IButton("button-delete");
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
            panel.history.saveEditId();
            for (final String s : args) {
                ((Trie) D).insert(s);
            }
            if (panel.pauses && !args.isEmpty()){
                panel.history.rewind();
            }
        } else if (evt.getSource() == findB) {
            final Vector<String> args = I.getVS();
            if (args.size() > 0) {
                panel.history.saveEditId();
                for (final String s : args) {
                    ((Trie) D).find(s);
                }
                if (panel.pauses){
                    panel.history.rewind();
                }
            }
        } else if (evt.getSource() == deleteB) {
            final Vector<String> args = I.getVS();
            if (args.size() > 0) {
                panel.history.saveEditId();
                for (final String s : args) {
                    ((Trie) D).delete(s);
                }
                if (panel.pauses){
                    panel.history.rewind();
                }
            }
        }
    }

    @Override
    public void setOtherEnabled(boolean enabled) {
        super.setOtherEnabled(enabled);
        insertB.setEnabled(enabled);
        findB.setEnabled(enabled);
        deleteB.setEnabled(enabled);
    }
}
