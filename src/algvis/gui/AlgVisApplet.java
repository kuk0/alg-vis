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

import java.awt.Color;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;

import algvis.core.DataStructures;
import algvis.core.MyParserDelegator;
import algvis.core.Settings;
import algvis.internationalization.Languages;

public class AlgVisApplet extends JApplet {
	private static final long serialVersionUID = -76009301274562874L;
	static final int WIDTH = 1080, HEIGHT = 680;

	@Override
	public void init() {
		MyParserDelegator.workaround();
		Fonts.init(getGraphics());
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.put("nimbusBase", new Color(0xBB, 0xC3, 0xFF));
					UIManager.put("TitledBorder.position", TitledBorder.CENTER);
					UIManager
							.put("nimbusBlueGrey", new Color(0xD1, 0xD1, 0xD1));
					UIManager.put("control", new Color(0xFA, 0xFA, 0xFA));
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		/**
		 * choose data structure depending on the "ds" parameter in the <applet>
		 * tag if ds is a number from 0 to N-1, we will have an applet with just
		 * a single data structure, otherwise, include all of them
		 */
		int ds = -1;
		String dsp = getParameter("ds");
		try {
			ds = Integer.parseInt(dsp);
			if (ds < 0 || ds >= DataStructures.N)
				ds = -1;
		} catch (NumberFormatException e) {
			ds = DataStructures.getIndex(dsp);
		}
		if (ds == -1) {
			// all data structures
			AlgVis A = new AlgVis(this.getRootPane(), getParameter("lang"));
			A.setSize(WIDTH, HEIGHT); // same size as defined in the HTML APPLET
			add(A);
			A.init();
		} else {
			// data structure ds
			Languages L = new Languages(getParameter("lang"));
			Settings S = new Settings(L);
			VisPanel P = DataStructures.createPanel(ds, S);
			P.setSize(WIDTH, HEIGHT); // same size as defined in the HTML APPLET
			if (P != null)
				add(P);
		}
	}
}
