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

package algvis.core.visual;

import algvis.gui.view.View;

import javax.swing.undo.StateEditable;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

public abstract class VisualElement implements StateEditable {
	Scene scene;
	protected int zDepth;
	protected final String hash = Integer.toString(hashCode());

	protected VisualElement(Scene scene, int zDepth) {
		this.scene = scene;
		this.zDepth = zDepth;
	}

	public Scene getScene() {
		return scene;
	}

	public int getZDepth() {
		return zDepth;
	}

//	public void setZDepth(int zDepth) {
//		if (zDepth != this.zDepth) {
//			scene.changeZDepth(this, this.zDepth, zDepth);
//			this.zDepth = zDepth;
//		}
//	}
	
	public void addToScene() {
		scene.add(this, zDepth);
	}
	
	public void removeFromScene() {
		scene.remove(this);
	}

	public void removeFromSceneNow() {
		scene.removeNow(this);
	}

	protected abstract void draw(View v);

	protected abstract void move();

	protected abstract Rectangle2D getBoundingBox();
	
	protected abstract void endAnimation();
	
	protected abstract boolean isAnimationDone();

	@Override
	public void storeState(Hashtable<Object, Object> state) {
//		HashtableStoreSupport.store(state, hash + "zDepth", zDepth);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
//		Object zDepth = state.get(hash + "zDepth");
//		if (zDepth != null) setZDepth((Integer) HashtableStoreSupport.restore(zDepth));
	}
}
