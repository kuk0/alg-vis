/*******************************************************************************
 * Copyright (c) 2012 Jakub Kov��, Katar�na Kotrlov�, Pavol Luk�a, Viktor Tomkovi�, Tatiana T�thov�
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
package algvis.pairingheap;

import algvis.gui.MeldablePQButtons;
import algvis.gui.VisPanel;
import algvis.internationalization.IComboBox;
import algvis.internationalization.ILabel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;

public class PairHeapButtons extends MeldablePQButtons implements
		ChangeListener {
	private static final long serialVersionUID = -5692673269308125662L;
	public IComboBox pairVariant;

	public PairHeapButtons(VisPanel M) {
		super(M);
	}

	@Override
	public JPanel initThirdRow() {
		JPanel P = new JPanel();

		ILabel fhLabel = new ILabel("pairing");
		String[] pairh = { "pairingnaive", "pairinglrrl" };
		// , "pairingfb", "pairingbf", "pairingmultipass", "pairinglazymulti" };

		pairVariant = new IComboBox(pairh);
		pairVariant.addActionListener(this);

		P.add(fhLabel);
		P.add(pairVariant);
		return P;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == pairVariant) {
			int i = pairVariant.getSelectedIndex();

			if ((i >= 0) && (i < 2)) { // (i < 6)){
				((PairingHeap) D).pairState = PairHeapDelete.Pairing.values()[i];
			}
		}
	}
}
