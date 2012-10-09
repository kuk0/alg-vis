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
package algvis.ds.priorityqueues;

import algvis.ui.VisPanel;

import javax.swing.*;
import javax.swing.event.ChangeListener;

public class MeldablePQButtonsNoDecr extends MeldablePQButtons implements ChangeListener {
	private static final long serialVersionUID = 1242711038059609653L;

	public MeldablePQButtonsNoDecr(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		super.actionButtons(P);
		P.remove(decrKeyB);
	}
}
