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

import algvis.core.AlgorithmAdapter;
import algvis.gui.VisPanel;
import algvis.internationalization.IButton;
import algvis.internationalization.ILabel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class MeldablePQButtons extends PQButtons implements ChangeListener {
	private static final long serialVersionUID = 1242711038059609653L;
    private IButton meldB;
	public JSpinner activeHeap;
	private ILabel activeLabel;

	public MeldablePQButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		super.actionButtons(P);

		meldB = new IButton("button-meld");
		meldB.setMnemonic(KeyEvent.VK_M);
		meldB.addActionListener(this);
		P.add(meldB);
	}

	@Override
	public void otherButtons(JPanel P) {
		activeHeap = new JSpinner(new SpinnerNumberModel(1, 1,
				MeldablePQ.numHeaps, 1));
		activeHeap.addChangeListener(this);
		activeLabel = new ILabel("activeheap");
		P.add(activeLabel);
		P.add(activeHeap);
		super.otherButtons(P);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == meldB) {
			Vector<Integer> args = I.getVI();
			args.add(-1);
			args.add(-1);
			((MeldablePQ) D).meld(args.get(0), args.get(1));	
		}
	}

	@Override
	public void setOtherEnabled(boolean enabled) {
		super.setOtherEnabled(enabled);
		meldB.setEnabled(enabled);
		activeHeap.setEnabled(enabled);
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == activeHeap) {
			if (panel.history.canRedo()) panel.newAlgorithmPool();
			final MeldablePQ H = ((MeldablePQ) D);
			D.start(new AlgorithmAdapter(panel) {
				@Override
				public void runAlgorithm() throws InterruptedException {
					H.lowlight();
					H.highlight((Integer) activeHeap.getValue());
				}
			});
			if (H.chosen != null)
				H.chosen.unmark();
		}
	}
}
