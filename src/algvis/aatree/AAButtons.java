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
package algvis.aatree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import algvis.gui.DictButtons;
import algvis.gui.VisPanel;
import algvis.internationalization.ICheckBox;

public class AAButtons extends DictButtons implements ActionListener {
	private static final long serialVersionUID = 5326663225787843118L;
	ICheckBox B23;

	public AAButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		B23 = new ICheckBox("mode23", false);
		B23.setMnemonic(KeyEvent.VK_2);
		B23.addActionListener(this);
		P.add(B23);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == B23) {
			((AA) M.D).setMode23(B23.isSelected());
		}
	}
}
