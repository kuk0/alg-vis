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

public class TrieHelpWord {
	private String s = "";
	private int x = 0;
	private int y = 0;
	private NodeColor c = NodeColor.INSERT;

	public TrieHelpWord(String s, int x, int y, NodeColor c) {
		setS(s);
		this.x = x;
		this.y = y;
		this.c = c;
	}

	public TrieHelpWord(String s) {
		setS(s);
	}

	public String getS() {
		return s;
	}

	public String setS(String s) {
		this.s = s;
		return s;
	}

	public NodeColor getC() {
		return c;
	}

	public void setC(NodeColor c) {
		this.c = c;
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

	public void goTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void goTo(Node v) {
		this.x = v.x;
		this.y = v.y;
	}

	public void goNextTo(Node v) {
		this.x = v.x + DataStructure.minsepx / 3;
		this.y = v.y;
	}

	public void setAndGoNT(String s, Node v) {
		setS(s);
		goNextTo(v);
	}

	public void draw(View v) {
		if (s.compareTo("") == 0) {
			return;
		}
		FontMetrics fm = v.getGraphics().getFontMetrics();
		int width = (fm.stringWidth(s) + 4) / 2;
		int height = (fm.getHeight() + 4) / 2;
		v.setColor(c.bgColor);
		v.fillRoundRectangle(x + width, y - height / 2, width, height, 3, 3);
		v.setColor(Color.BLACK);
		v.drawRoundRectangle(x + width, y - height / 2, width, height, 3, 3);
		v.setColor(c.fgColor);
		v.drawString(s, x + width, y - height / 2, Fonts.TYPEWRITER);
	}
}
