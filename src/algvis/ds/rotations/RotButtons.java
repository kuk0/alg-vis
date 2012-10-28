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
package algvis.ds.rotations;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.internationalization.IButton;
import algvis.internationalization.ICheckBox;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

public class RotButtons extends Buttons {
	private static final long serialVersionUID = 3851020370059429766L;
	private IButton rotB;
	private ICheckBox order;
	private ICheckBox subtrees;

	public RotButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		rotB = new IButton("button-rotate");
		rotB.setMnemonic(KeyEvent.VK_T);
		rotB.addActionListener(this);
		P.add(rotB);
	}

	@Override
	public void otherButtons(JPanel P) {
		order = new ICheckBox("show-order", false);
		order.setMnemonic(KeyEvent.VK_O);
		order.addActionListener(this);
		P.add(order);

		subtrees = new ICheckBox("show-subtrees", false);
		subtrees.setMnemonic(KeyEvent.VK_S);
		subtrees.addActionListener(this);
		P.add(subtrees);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		final Rotations R = (Rotations) D;
		if (evt.getSource() == rotB) {
			if (panel.history.canRedo())
				panel.newAlgorithmPool();
			Vector<Integer> args = I.getNonEmptyVI();
			for (int x : args) {
				R.rotate(x);
			}
		} else if (evt.getSource() == order) {
			R.T.order = order.isSelected();
		} else if (evt.getSource() == subtrees) {
			R.subtrees = subtrees.isSelected();
		}
	}

	@Override
	public void setOtherEnabled(boolean enabled) {
		super.setOtherEnabled(enabled);
		rotB.setEnabled(enabled);
	}
}
