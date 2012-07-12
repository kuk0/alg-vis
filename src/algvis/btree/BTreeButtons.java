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
package algvis.btree;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import algvis.gui.DictButtons;
import algvis.gui.VisPanel;
import algvis.internationalization.ILabel;

public class BTreeButtons extends DictButtons implements ChangeListener {
	private static final long serialVersionUID = -4573594717377516312L;
	JSpinner OS;
	ILabel orderLabel;

	public BTreeButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		OS = new JSpinner(new SpinnerNumberModel(5, 3, 20, 1));
		OS.addChangeListener(this);
		orderLabel = new ILabel("btreeorder");
		P.add(orderLabel);
		P.add(OS);
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == OS) {
			((BTree) D).order = (Integer) OS.getValue();
			((BTree) D).clear();
			((BTree) D).reposition();
		}
	}
}
