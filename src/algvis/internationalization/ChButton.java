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

import javax.swing.*;
import java.awt.*;

public class ChButton extends JButton {
	private static final long serialVersionUID = 6239957285446549335L;

	ChButton(String text) {
		super(text);
	}

	void refresh() {
		FontMetrics metrics = getFontMetrics(getFont());
		int width = metrics.stringWidth(getText());
		int height = metrics.getHeight();
		Dimension newDimension = new Dimension(width + 40, height + 10);
		setPreferredSize(newDimension);
		setBounds(new Rectangle(getLocation(), getPreferredSize()));
	}
}
