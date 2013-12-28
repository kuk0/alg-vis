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

import algvis.core.StringUtils;
import algvis.internationalization.Stringable;
import algvis.ui.view.CornerEnum;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;

public class TextBubble extends VisualElement {
	Stringable s;
	int x, y, w, alpha;
	int state; // 0 = fade-in; 1 = normal; 2 = fade-out;
	CornerEnum pos;

	public TextBubble(Stringable s, int x, int y, int w, CornerEnum pos) {
		super(0);
		this.s = s;
		this.x = x;
		this.y = y;
		this.w = w;
		this.alpha = 0;
		this.pos = pos;
	}

	@Override
	public void draw(View V) {
		V.drawTextBubble(StringUtils.unHtml(s.getString()), x, y, w, alpha, pos);
	} // TODO: unHtml only until we get rid of the commentary

	@Override
	public void move() {
		switch (state) {
		case 0:
			alpha += 21;
			if (alpha >= 0xf0)
				state = 1;
			break;
		case 1:
			break;
		case 2:
			alpha -= 21;
			if (alpha <= 0) {
				alpha = 0;
				state = 3;
			}
			break;
		}
	}

	@Override
	protected void endAnimation() {
		state = 2;
	}

/*	public void hide() {
		state = 2;
	}*/

	@Override
	public Rectangle2D getBoundingBox() {
		return null;
	}

	@Override
	protected boolean isAnimationDone() {
		return state == 3;
	}
}