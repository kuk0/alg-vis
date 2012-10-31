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

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.view.View;

public class Scene extends VisualElement {
	public static final int MAXZ = 10, MIDZ = 5;
	private final List<HashSet<VisualElement>> elements = new ArrayList<HashSet<VisualElement>>();
	private final RemoveManager removeManager = new RemoveManager();

	public Scene() {
		super(0);
		for (int i = 0; i < MAXZ; ++i) {
			elements.add(new HashSet<VisualElement>());
		}
	}

	public synchronized void add(VisualElement element) {
		this.add(element, element.getZDepth());
	}
	
	public synchronized void add(VisualElement element, int zDepth) {
		removeManager.add(element, zDepth);
	}

	/**
	 * Element is removed after it ends its animation.
	 * 
	 * @param element
	 */
	public synchronized void remove(VisualElement element) {
		removeManager.remove(element);
	}

	/**
	 * Removes element immediately. Don't wait until element ends its animation.
	 * 
	 * @param element
	 */
	public synchronized void removeNow(VisualElement element) {
		elements.get(element.getZDepth()).remove(element);
		if (removeManager.toRemove.contains(element)) {
			removeManager.toRemove.remove(element);
		}
	}

	// public synchronized void changeZDepth(VisualElement element, int from,
	// int to) {
	// Set<VisualElement> set = elements.get(from);
	// if (set.contains(element)) {
	// set.remove(element);
	// elements.get(to).add(element);
	// }
	// }

	public void draw(View V) {
		for (int i = MAXZ - 1; i >= 0; --i) {
			synchronized (this) {
				for (VisualElement e : elements.get(i)) {
					e.draw(V);
					// Rectangle2D r = e.getBoundingBox();
					// if (r != null) {
					// V.setColor(Color.RED);
					// V.drawRectangle(r);
					// }
				}
			}
		}
	}

	public synchronized void move() {
		for (Set<VisualElement> set : elements) {
			for (VisualElement e : set) {
				e.move();
			}
		}
	}

	public synchronized Rectangle2D getBoundingBox() {
		Rectangle2D retVal = null;
		for (Set<VisualElement> set : elements) {
			for (VisualElement e : set) {
				Rectangle2D eBB = e.getBoundingBox();
				if (retVal == null) {
					retVal = eBB;
				} else if (eBB != null) {
					retVal = retVal.createUnion(eBB);
				}
			}
		}
		return retVal;
	}

	@Override
	public synchronized void endAnimation() {
		for (Set<VisualElement> set : elements) {
			for (VisualElement e : set) {
				e.endAnimation();
			}
		}
	}

	@Override
	public synchronized boolean isAnimationDone() {
		boolean retVal = true;
		for (Set<VisualElement> set : elements) {
			for (VisualElement e : set) {
				retVal &= e.isAnimationDone();
			}
		}
		return retVal;
	}

	@Override
	public synchronized void storeState(Hashtable<Object, Object> state) {
		List<Set<VisualElement>> elementsClone = new ArrayList<Set<VisualElement>>();
		for (HashSet<VisualElement> set : elements) {
			elementsClone.add((Set<VisualElement>) set.clone());
		}
		for (VisualElement element : removeManager.toRemove) {
			elementsClone.get(element.getZDepth()).remove(element);
		}
		for (int i = 0; i < MAXZ; ++i) {
			HashtableStoreSupport.store(state, hash + "elements" + i,
					elementsClone.get(i));
		}

		for (Set<VisualElement> set : elements) {
			for (VisualElement e : set) {
				e.storeState(state);
			}
		}
	}

	@Override
	public synchronized void restoreState(Hashtable<?, ?> state) {
		for (int i = 0; i < MAXZ; ++i) {
			Set<VisualElement> setI = (Set<VisualElement>) state.get(hash
					+ "elements" + i);
			if (setI != null) {
				for (VisualElement e : elements.get(i)) {
					if (setI.contains(e) && removeManager.toRemove.contains(e)) {
						removeManager.toRemove.remove(e);
					} else if (!setI.contains(e)
							&& !removeManager.toRemove.contains(e)) {
						remove(e);
					}
				}

				for (VisualElement e : setI) {
					if (!elements.get(i).contains(e)) {
						add(e, e.getZDepth());
						// if (e instanceof Node) {
						// System.out.println(((Node) e).getKey());
						// System.out.println(((Node) e).getZDepth());
						// }
					}
				}
			}
		}

		for (Set<VisualElement> set : elements) {
			for (VisualElement e : set) {
				e.restoreState(state);
			}
		}
	}

	public synchronized void clear() {
		for (VisualElement e : removeManager.toRemove) {
			removeManager.toRemove.remove(e);
		}
		for (Set<VisualElement> set : elements) {
			set.clear();
		}
	}

	private class RemoveManager {
		public final Set<VisualElement> toRemove = new HashSet<VisualElement>();

		private RemoveManager() {
			// this thread wait until element ends its animation and then
			// removes it from scene
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						synchronized (Scene.this) {
							// int poc = 0;
							// for(Set<VisualElement> set : elements) poc +=
							// set.size();
							// System.out.println(toRemove.size());
							// System.out.println("elemSIZE: " + poc);
							// System.out.println("elemSIZE2: " +
							// elements.get(5).size());
							Iterator<VisualElement> iterator = toRemove
									.iterator();
							while (iterator.hasNext()) {
								VisualElement element = iterator.next();
								if (element.isAnimationDone()) {
									iterator.remove();
									if (element instanceof Node) {
										// System.out.println("removed: " +
										// ((Node) element).getKey());
										// System.out.println("removed: " +
										// element.getZDepth());
										// System.out.println("removed: " +
										// ((Node) element).state);
									}
									Set<VisualElement> set = elements
											.get(element.getZDepth());
									set.remove(element);
								}
							}
						}
						synchronized (this) {
							try {
								wait(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}

		public void add(VisualElement element, int zDepth) {
			if (toRemove.contains(element)) {
				toRemove.remove(element);
			}
			if (zDepth < 0)
				zDepth = 0;
			if (zDepth >= MAXZ)
				zDepth = MAXZ - 1;
			Set<VisualElement> set = elements.get(zDepth);
			set.add(element);
		}

		public void remove(VisualElement element) {
			toRemove.add(element);
		}
	}
}
