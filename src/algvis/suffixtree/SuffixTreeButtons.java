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
package algvis.suffixtree;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.Buttons;
import algvis.core.VisPanel;
import algvis.internationalization.IButton;

public class SuffixTreeButtons extends Buttons {
	private static final long serialVersionUID = -368670840648549217L;
	IButton insertB/* , findB */;

	public SuffixTreeButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton(M.S.L, "button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		/*
		 * findB = new IButton(M.S.L, "button-find");
		 * findB.setMnemonic(KeyEvent.VK_F); findB.addActionListener(this);
		 */
		P.add(insertB);
		// P.add(findB);
	}

	@Override
	public void initRandom() {
		random = new IButton(M.S.L, "button-random");
		random.setMnemonic(KeyEvent.VK_R);
		random.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == insertB) {
			final Vector<String> args = I.getVS();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (String s : args) {
						((SuffixTree) D).insert(s);
					}
				}
			});
			t.start();
		}
		/*
		 * else if (evt.getSource() == findB) { final Vector<String> args =
		 * I.getVS(); if (args.size() > 0) { Thread t = new Thread(new
		 * Runnable() {
		 * 
		 * @Override public void run() { for (String s : args) { ((SuffixTree)
		 * D).find(s); } } }); t.start(); } }
		 */
	}

	@Override
	public void enableAll() {
		super.enableAll();
		insertB.setEnabled(true);
		// findB.setEnabled(true);
	}

	@Override
	public void disableAll() {
		super.disableAll();
		insertB.setEnabled(false);
		// findB.setEnabled(false);
	}
}
