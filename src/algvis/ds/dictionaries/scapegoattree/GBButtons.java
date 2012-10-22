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
package algvis.ds.dictionaries.scapegoattree;

import algvis.ui.DictButtons;
import algvis.ui.VisPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GBButtons extends DictButtons implements ChangeListener {
	private static final long serialVersionUID = -4200856610929537432L;
	private JSpinner AS;
	private JLabel alpha;

	public GBButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		alpha = new JLabel("\u03B1:");
		AS = new JSpinner(new SpinnerNumberModel(1.3, 1.01, 5, 0.1));
		AS.addChangeListener(this);
		P.add(alpha);
		P.add(AS);
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == AS) {
			((GBTree) D).alpha = (Double) AS.getValue();
		}
	}
}
