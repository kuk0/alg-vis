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
package algvis.ds.suffixtree;

import algvis.internationalization.IButton;
import algvis.internationalization.ICheckBox;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class SuffixTreeButtons extends Buttons {
	private static final long serialVersionUID = -368670840648549217L;
	private IButton insertB;
    private IButton findB;
	private ICheckBox implicitB;

	public SuffixTreeButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton("button-create-st");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		findB = new IButton("button-find");
		findB.setMnemonic(KeyEvent.VK_F);
		findB.addActionListener(this);

		P.add(insertB);
		P.add(findB);
	}

	@Override
	public void otherButtons(JPanel P) {
		implicitB = new ICheckBox("implicit", false);
		implicitB.setMnemonic(KeyEvent.VK_I);
		implicitB.addActionListener(this);
		P.add(implicitB);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == insertB) {
			if (panel.history.canRedo()) panel.newAlgorithmPool();
			Vector<String> args = I.getVABS();
			for (String s : args) {
				((SuffixTree) D).insert(s);
			}
		} else if (evt.getSource() == findB) {
			if (panel.history.canRedo()) panel.newAlgorithmPool();
			Vector<String> args = I.getVS();
			if (args.size() > 0) {
				for (String s : args) {
					((SuffixTree) D).find(s);
				}
			}
		} else if (evt.getSource() == implicitB) {
			SuffixTreeNode.implicitNodes = implicitB.isSelected();
		}

	}

	@Override
	public void setOtherEnabled(boolean enabled) {
		super.setOtherEnabled(enabled);
		insertB.setEnabled(enabled);
		findB.setEnabled(enabled);
	}
}
