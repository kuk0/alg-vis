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
package algvis.redblacktree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import algvis.gui.DictButtons;
import algvis.gui.VisPanel;
import algvis.internationalization.ICheckBox;

public class RBButtons extends DictButtons implements ActionListener {
	private static final long serialVersionUID = 5601437441473816995L;
	ICheckBox B24;

	public RBButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		B24 = new ICheckBox(M.S.L, "mode234", false);
		B24.setMnemonic(KeyEvent.VK_2);
		B24.addActionListener(this);
		P.add(B24);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == B24) {
			((RB) M.D).mode24 = B24.isSelected();
			((RB) M.D).reposition();
		}
	}
}
