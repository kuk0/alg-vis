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
package algvis.ui;

import algvis.core.Settings;
import algvis.internationalization.ILabel;
import algvis.internationalization.LanguageListener;

import java.awt.BorderLayout;
import java.util.Hashtable;

import javax.swing.undo.StateEditable;

public abstract class NewVisPanel extends VisPanel implements LanguageListener,
		StateEditable {
	private static final long serialVersionUID = -5866649744399813386L;

	protected NewVisPanel(Settings S) {
		super(S);
		//init();
	}

	@Override
	protected void init() {
		setLayout(new BorderLayout(0, 0));
		initScreen();
		statusBar = new ILabel("EMPTYSTR");
		initDS();

		add(screen, BorderLayout.CENTER);
		add(buttons, BorderLayout.PAGE_END);
		//add(statusBar, BorderLayout.PAGE_END);

		screen.setDS(D);
		languageChanged();
	}

	@Override
	public void refresh() {
		buttons.refresh();
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		buttons.storeState(state);
		scene.storeState(state);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		buttons.restoreState(state);
		scene.restoreState(state);
	}
}
