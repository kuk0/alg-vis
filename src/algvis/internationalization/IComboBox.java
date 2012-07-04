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
package algvis.internationalization;

import java.awt.Color;

import javax.swing.JComboBox;

public class IComboBox extends JComboBox implements LanguageListener {
	private static final long serialVersionUID = 8795452558528688577L;
	private final Languages L;
	private final String[] choices;

	public IComboBox(Languages L, String[] choices) {
		super(choices);
		this.L = L;
		this.choices = choices;
		languageChanged();
		L.addListener(this);
		setBackground(Color.WHITE);
	}

	@Override
	public void languageChanged() {
		removeAllItems();
		for (int i = 0; i < choices.length; ++i) {
			this.addItem(L.getString(choices[i]));
		}
	}
}
