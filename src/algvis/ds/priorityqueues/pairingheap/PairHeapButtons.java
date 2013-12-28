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
package algvis.ds.priorityqueues.pairingheap;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import algvis.ds.priorityqueues.MeldablePQButtons;
import algvis.internationalization.IComboBox;
import algvis.internationalization.ILabel;
import algvis.ui.VisPanel;

public class PairHeapButtons extends MeldablePQButtons implements
		ChangeListener {
	private static final long serialVersionUID = -5692673269308125662L;
	public IComboBox pairVariant;

	public PairHeapButtons(VisPanel M) {
		super(M);
	}

	@Override
	public JPanel initThirdRow() {
		final JPanel P = new JPanel();

		final ILabel fhLabel = new ILabel("pairing");
		final String[] pairh = { "pairingnaive", "pairinglrrl" };
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
			final int i = pairVariant.getSelectedIndex();

			if ((i >= 0) && (i < 2)) { // (i < 6)){
				((PairingHeap) D).pairState = PairHeapDelete.Pairing.values()[i];
			}
		}
	}
}
