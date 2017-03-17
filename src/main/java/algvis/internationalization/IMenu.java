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

import javax.swing.JMenu;

public class IMenu extends JMenu implements LanguageListener {
    private static final long serialVersionUID = -6631532284442911505L;
    private Stringable t;

    public IMenu(Stringable text) {
        super(text.getString());
        this.t = text;
        Languages.addListener(this);
    }

    public IMenu(String text) {
        this(new IString(text));
    }

    public void setT(Stringable text) {
        t = text;
        setText(t.getString());
    }

    @Override
    public void languageChanged() {
        setText(t.getString());
    }
}
