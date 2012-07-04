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
package algvis.rotations;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.gui.Buttons;
import algvis.gui.VisPanel;
import algvis.internationalization.IButton;
import algvis.internationalization.ICheckBox;

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
		rotB = new IButton(M.S.L, "button-rotate");
		rotB.setMnemonic(KeyEvent.VK_T);
		rotB.addActionListener(this);
		P.add(rotB);
	}


	@Override
	public void otherButtons(JPanel P) {
		order = new ICheckBox(M.S.L, "show-order", false);
		order.setMnemonic(KeyEvent.VK_O);
		order.addActionListener(this);
		P.add(order);

		subtrees = new ICheckBox(M.S.L, "show-subtrees", false);
		subtrees.setMnemonic(KeyEvent.VK_S);
		subtrees.addActionListener(this);
		P.add(subtrees);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		final Rotations R = (Rotations) D;
		if (evt.getSource() == rotB) {
			final Vector<Integer> args = I.getNonEmptyVI();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int x : args) {
						R.rotate(x);
					}
				}
			});
			t.start();
		} else if (evt.getSource() == order) {
			R.T.order = order.isSelected();
		} else if (evt.getSource() == subtrees) {
			R.subtrees = subtrees.isSelected(); 
		}
	}

	@Override
	public void enableNext() {
		super.enableNext();
		rotB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		rotB.setEnabled(true);
	}
}
