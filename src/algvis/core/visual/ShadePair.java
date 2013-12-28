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

package algvis.core.visual;

import java.awt.geom.Rectangle2D;

import algvis.core.Node;
import algvis.ui.view.View;

public class ShadePair extends VisualElement {
	Node u, v;

	public ShadePair(Node u, Node v) {
		super(Scene.MAXZ - 1);
		this.u = u;
		this.v = v;
	}

	@Override
	protected void draw(View V) {
		V.drawWideLine(u.x, u.y, v.x, v.y);
	}

	@Override
	protected void move() {
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return null;
	}
}
