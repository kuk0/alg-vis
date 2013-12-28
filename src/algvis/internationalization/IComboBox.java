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
package algvis.internationalization;

import java.awt.Color;

import javax.swing.JComboBox;

public class IComboBox extends JComboBox<String> implements LanguageListener {
	private static final long serialVersionUID = 8795452558528688577L;
	private Stringable[] choices;

	public IComboBox(Stringable[] choices) {
		super();
		setChoices(choices);
	}

	public IComboBox(String[] choices) {
		super();
		final Stringable[] ch = new Stringable[choices.length];
		for (int i = 0; i < choices.length; ++i) {
			ch[i] = new IString(choices[i]);
		}
		setChoices(ch);
	}

	public void setChoices(Stringable[] choices) {
		this.choices = choices;
		languageChanged();
		Languages.addListener(this);
		setBackground(Color.WHITE);
	}

	@Override
	public void languageChanged() {
		removeAllItems();
		for (final Stringable choice : choices) {
			this.addItem(choice.getString());
		}
	}
}
