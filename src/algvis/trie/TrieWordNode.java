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
package algvis.trie;

import java.awt.Color;
import java.awt.FontMetrics;

import algvis.core.DataStructure;
import algvis.core.Fonts;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.View;

public class TrieWordNode extends Node {
	private String s = "";

	public TrieWordNode(DataStructure D, String s, int x, int y, NodeColor c) {
		setS(s);
		this.x = x;
		this.y = y;
		setColor(c);
		this.D = D;
	}

	public TrieWordNode(DataStructure D, String s) {
		this(D, s, 0, 0, NodeColor.BLACK);
	}

	public String getS() {
		return s;
	}

	public String setS(String s) {
		this.s = s;
		return s;
	}

	/**
	 * 
	 * @param x
	 *            how many chars will be cut from the beginning of the string
	 */
	public String cut(int x) {
		s = s.substring(x, s.length());
		return s;
	}

	@Override
	public void goNextTo(Node v) {
		goTo(v.tox + DataStructure.minsepx / 3, v.toy);
	}

	public void setAndGoNextTo(String s, Node v) {
		setS(s);
		goNextTo(v);
	}

	public String cutOneAndGoNextTo(Node v) {
		String result = cut(1);
		goNextTo(v);
		return result;
	}

	@Override
	public void draw(View v) {
		if (s.compareTo("") == 0) {
			return;
		}
		FontMetrics fm = v.getGraphics().getFontMetrics();
		int width = (fm.stringWidth(s) + 4) / 2;
		int height = (fm.getHeight() + 4) / 2;
		v.setColor(getColor().bgColor);
		v.fillRoundRectangle(x + width, y - height / 2, width, height, 3, 3);
		v.setColor(Color.BLACK);
		v.drawRoundRectangle(x + width, y - height / 2, width, height, 3, 3);
		v.setColor(getColor().fgColor);
		v.drawString(s, x + width, y - height / 2, Fonts.TYPEWRITER);
	}
}
