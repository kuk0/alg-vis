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

public class IButton extends ChButton implements LanguageListener {
	private static final long serialVersionUID = -6020341462591231389L;
	private Stringable t;

	public IButton(Stringable text) {
		super(text.getString());
		t = text;
		Languages.addListener(this);
	}

	public IButton(String text) {
		this(new IString(text));
	}

	public void setT(Stringable text) {
		t = text;
		setText(t.getString());
		refresh();
	}

	public void setT(String text) {
		setT(new IString(text));
	}

	@Override
	public void languageChanged() {
		setText(t.getString());
		refresh();
	}
}
