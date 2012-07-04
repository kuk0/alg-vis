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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.internationalization.ILabel;
import algvis.internationalization.LanguageListener;
import algvis.scenario.Scenario;

public abstract class VisPanel extends JPanel implements LanguageListener {
	private static final long serialVersionUID = 5104769085118210624L;
	public static Class<? extends DataStructure> DS;

	// aplet pozostava z piatich zakladnych veci:
	public Buttons B; // gombikov (dolu)
	public Commentary C; // komentara (vpravo)
	public DataStructure D; // datovej struktury
	public Screen screen; // obrazovky v strede
	public ILabel statusBar; // a status baru
	public final Settings S;
	private TitledBorder border;

	public boolean pause = true, small = false;
	public final Scenario scenario = new Scenario(this, getName());

	protected VisPanel(Settings S, boolean isScenarioEnabled) {
		this.S = S;
		scenario.enable(isScenarioEnabled);
		init();
	}

	private void init() {
		this.setLayout(new GridBagLayout());
		JPanel screenP = initScreen();
		JScrollPane commentary = initCommentary();
		statusBar = new ILabel(S.L, "EMPTYSTR");
		initDS();

		GridBagConstraints cs = new GridBagConstraints();
		cs.gridx = 0;
		cs.gridy = 0;
		cs.fill = GridBagConstraints.BOTH;
		add(screenP, cs);

		GridBagConstraints cc = new GridBagConstraints();
		cc.gridx = 1;
		cc.gridy = 0;
		cc.gridheight = 2;
		cc.fill = GridBagConstraints.VERTICAL;
		add(commentary, cc);

		GridBagConstraints cb = new GridBagConstraints();
		cb.gridx = 0;
		cb.gridy = 1;
		cb.fill = GridBagConstraints.HORIZONTAL;
		add(B, cb);

		GridBagConstraints csb = new GridBagConstraints();
		csb.gridx = 0;
		csb.gridy = 2;
		csb.fill = GridBagConstraints.HORIZONTAL;
		add(statusBar, csb);

		screen.setDS(D);
		screen.start();
		languageChanged();
		B.I.requestFocusInWindow();
	}

	private JPanel initScreen() {
		JPanel screenP = new JPanel();
		screenP.setLayout(new BorderLayout());
		screen = new Screen() {
			private static final long serialVersionUID = 2196788670749006364L;

			@Override
			public Dimension getMaximumSize() {
				return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(550, 400);
			}

			@Override
			public Dimension getMinimumSize() {
				return new Dimension(550, 400);
				// return new Dimension(300, 100);
			}
		};
		screenP.add(screen, BorderLayout.CENTER);

		border = BorderFactory.createTitledBorder("");
		border.setTitleJustification(TitledBorder.CENTER);
		border.setTitleFont(new Font("Sans-serif", Font.ITALIC, 12));
		S.L.addListener(this);
		screenP.setBorder(BorderFactory.createCompoundBorder(border,
				BorderFactory.createEmptyBorder(0, 5, 5, 5)));
		return screenP;
	}

	private JScrollPane initCommentary() {
		// Commentary
		JScrollPane SP = new JScrollPane() {
			private static final long serialVersionUID = -8618469733328277117L;

			@Override
			public Dimension getMaximumSize() {
				return new Dimension(400, 680);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 600);
			}

			@Override
			public Dimension getMinimumSize() {
				return new Dimension(250, 600);
				// return new Dimension(200, 530);
			}
		};
		C = new Commentary(this, S.L, SP);
		SP.setViewportView(C);
		// JPanel CP = new JPanel();
		// CP.add(SP);
		SP.setBorder(BorderFactory.createTitledBorder(""));
		return SP;
	}

	protected abstract void initDS();

	/*
	 * public void showStatus (String t) { statusBar.setT(t); }
	 */

	@Override
	public void languageChanged() {
		border.setTitle("    " + S.L.getString(D.getName()) + "    ");
	}
	
	public void setOnAir(boolean onAir) {
		if (!onAir) {
			screen.suspend();
		} else {
			screen.resume();
		}
	}
}
