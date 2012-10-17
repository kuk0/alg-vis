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
package algvis.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.Dictionary;
import algvis.internationalization.IButton;
import algvis.scenario.Scenario;

/**
 * The Class DictButtons. All dictionary data structures need buttons "Insert",
 * "Find", and "Delete".
 */
public class DictButtons extends Buttons {
	private static final long serialVersionUID = 8331529914377645715L;
	private IButton insertB;
    private IButton findB;
    private IButton deleteB;

	public DictButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton("button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		findB = new IButton("button-find");
		findB.setMnemonic(KeyEvent.VK_F);
		findB.addActionListener(this);

		deleteB = new IButton("button-delete");
		deleteB.setMnemonic(KeyEvent.VK_D);
		deleteB.addActionListener(this);

		P.add(insertB);
		P.add(findB);
		P.add(deleteB);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == insertB) {
			final Vector<Integer> args = I.getNonEmptyVI();
			D.M.scenario.traverser.startNew(new Runnable() {
				@Override
				public void run() {
					boolean p = M.pause;
					int n = args.size();
					int i = 0;
					D.M.scenario.enableAdding(false);
					M.C.enableUpdating(p);
					for (; i < n - Scenario.maxAlgorithms; ++i) {
						if (M.pause != p) {
							M.C.enableUpdating(p = M.pause);
						}
						D.insert(args.elementAt(i));
					}
					D.M.scenario.enableAdding(true);
					for (; i < n; ++i) {
						if (M.pause != p) {
							M.C.enableUpdating(p = M.pause);
						}
						D.insert(args.elementAt(i));
					}
					M.C.enableUpdating(true);
					M.C.update();
				}
			}, true);
			if (D.M.scenario.isEnabled() && args.size() == 1) {
				startLastAlg();
			}
		} else if (evt.getSource() == findB) {
			final Vector<Integer> args = I.getVI();
			if (args.size() > 0) {
				for (final int x : args) {
					D.M.scenario.traverser.startNew(new Runnable() {
						@Override
						public void run() {
							((Dictionary) D).find(x);
						}
					}, true);
					if (D.M.scenario.isEnabled()) {
						startLastAlg();
					}
				}
			}
		} else if (evt.getSource() == deleteB) {
			final Vector<Integer> args = I.getVI();
			if (args.size() > 0) {
				for (final int x : args) {
					D.M.scenario.traverser.startNew(new Runnable() {
						@Override
						public void run() {
							((Dictionary) D).delete(x);
						}
					}, true);
					if (D.M.scenario.isEnabled()) {
						startLastAlg();
					}
				}
			}
		}
	}

	@Override
	public void enableAll() {
		super.enableAll();
		insertB.setEnabled(true);
		findB.setEnabled(true);
		deleteB.setEnabled(true);
	}

	@Override
	public void disableAll() {
		super.disableAll();
		insertB.setEnabled(false);
		findB.setEnabled(false);
		deleteB.setEnabled(false);
	}
}
