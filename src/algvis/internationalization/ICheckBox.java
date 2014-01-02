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

import javax.swing.JCheckBox;

public class ICheckBox extends JCheckBox implements LanguageListener {
    private static final long serialVersionUID = -8231264680063436446L;
    private final Stringable t;

    public ICheckBox(Stringable title, boolean on) {
        super(title.getString(), on);
        t = title;
        Languages.addListener(this);
    }

    public ICheckBox(String title, boolean on) {
        this(new IString(title), on);
    }

    @Override
    public void languageChanged() {
        setText(t.getString());
    }
}
