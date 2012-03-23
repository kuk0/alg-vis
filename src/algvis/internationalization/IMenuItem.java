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

import javax.swing.JMenuItem;

public class IMenuItem extends JMenuItem implements LanguageListener {
	private static final long serialVersionUID = -6522159616479156702L;
	Languages L;
	String t;

	public IMenuItem(Languages L, String text) {
		super(L.getString(text));
		this.L = L;
		this.t = text;
		L.addListener(this);
	}

	public IMenuItem(Languages L, String text, int K) {
		super(L.getString(text), K);
		this.L = L;
		this.t = text;
		L.addListener(this);
	}

	public void setT(String text) {
		t = text;
		setText(L.getString(t));
	}

	@Override
	public void languageChanged() {
		if (t.equals("bst"))
			System.out.println("bst");
		if (t.equals("layout-simple"))
			System.out.println("tu");
		setText(L.getString(t));
	}
}
