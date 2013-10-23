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

import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Scene extends VisualElement {
	public static final int MAXZ = 10, MIDZ = 5;
	private final List<HashSet<VisualElement>> elements = new ArrayList<HashSet<VisualElement>>();
    public final Set<VisualElement> elementsToRemove = new HashSet<VisualElement>();
	private final List<VisualElement> temporaryElements = new ArrayList<VisualElement>();

	public Scene() {
		super(0);
		for (int i = 0; i < MAXZ; ++i) {
			elements.add(new HashSet<VisualElement>());
		}
        initRemoverThread();
	}

    private void initRemoverThread() {
        // this thread waits until an element ends its animation and then
        // removes it from the scene
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (Scene.this) {
                        // int poc = 0;
                        // for(Set<VisualElement> set : elements) poc +=
                        // set.size();
                        // System.out.println(elementsToRemove.size());
                        // System.out.println("elemSIZE: " + poc);
                        // System.out.println("elemSIZE2: " +
                        // elements.get(5).size());
                        final Iterator<VisualElement> iterator = elementsToRemove
                                .iterator();
                        while (iterator.hasNext()) {
                            final VisualElement element = iterator.next();
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
                                final Set<VisualElement> set = elements
                                        .get(element.getZDepth());
                                set.remove(element);
                            }
                        }
                    }
                    synchronized (this) {
                        try {
                            wait(500);
                        } catch (final InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private Set<VisualElement> elementsAtDepth(int z) {
        return elements.get(z);
    }

	public synchronized void add(VisualElement element) {
		this.add(element, element.getZDepth());
	}

	public synchronized void add(VisualElement element, int zDepth) {
        if (elementsToRemove.contains(element)) {
            elementsToRemove.remove(element);
        }
        if (zDepth < 0) {
            zDepth = 0;
        }
        if (zDepth >= MAXZ) {
            zDepth = MAXZ - 1;
        }
        elementsAtDepth(zDepth).add(element);
    }

	public synchronized void addUntilNext(VisualElement element) {
		this.add(element, element.getZDepth());
		temporaryElements.add(element);
	}

	public synchronized void next() {
        for (VisualElement e : temporaryElements) {
            e.endAnimation();
            elementsToRemove.add(e);
        }
        temporaryElements.clear();
    }

	/**
	 * Remove element after it ends its animation.
	 *
	 * @param element
	 */
	public synchronized void remove(VisualElement element) {
        elementsToRemove.add(element);
	}

	/**
	 * Remove element immediately. Do not wait until it ends its animation.
	 *
	 * @param element
	 */
	public synchronized void removeNow(VisualElement element) {
		elements.get(element.getZDepth()).remove(element);
		if (elementsToRemove.contains(element)) {
			elementsToRemove.remove(element);
		}
	}

    /**
     * Draw all elements on the scene.
     */
     @Override
	public void draw(View V) {
		for (int i = MAXZ - 1; i >= 0; --i) {
			synchronized (this) {
				for (final VisualElement e : elementsAtDepth(i)) {
					e.draw(V);
					/* // testing bounding boxes
					Rectangle2D r = e.getBoundingBox();
					if (r != null) {
						V.setColor(Color.RED);
						V.drawRectangle(r);
					}
					*/
				}
			}
		}
	}

    /**
     * Move all elements on the scene.
     */
	@Override
	public synchronized void move() {
		for (final Set<VisualElement> set : elements) {
			for (final VisualElement e : set) {
				e.move();
			}
		}
	}

	@Override
	public synchronized Rectangle2D getBoundingBox() {
		Rectangle2D retVal = null;
		for (final Set<VisualElement> set : elements) {
			for (final VisualElement e : set) {
				final Rectangle2D eBB = e.getBoundingBox();
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
		for (final Set<VisualElement> set : elements) {
			for (final VisualElement e : set) {
				e.endAnimation();
			}
		}
	}

	@Override
	public synchronized boolean isAnimationDone() {
		for (final Set<VisualElement> set : elements) {
			for (final VisualElement e : set) {
				if (!e.isAnimationDone()) {
                    return false;
                }
			}
		}
		return true;
	}

	@Override
	public synchronized void storeState(Hashtable<Object, Object> state) {
        /*
        TODO: the code below is soo uggly and inefficient...
              we would better like sth. like this:
              or store only the added/removed elements
        for (int i = 0; i < MAXZ; ++i) {
            for (final VisualElement element : elementsAtDepth(i)) {
                if (!elementsToRemove.contains(element)) {
                    HashtableStoreSupport.store(state, hash + "elements" + i + "-" + element.hash,
                            element);
                }
                element.storeState(state);
            }
        } */
        final List<Set<VisualElement>> elementsClone = new ArrayList<Set<VisualElement>>();
		for (final HashSet<VisualElement> set : elements) {
			elementsClone.add((Set<VisualElement>) set.clone());
		}
		for (final VisualElement element : elementsToRemove) {
			elementsClone.get(element.getZDepth()).remove(element);
		}
		for (int i = 0; i < MAXZ; ++i) {
			HashtableStoreSupport.store(state, hash + "elements" + i,
					elementsClone.get(i));
		}

		for (final Set<VisualElement> set : elements) {
			for (final VisualElement e : set) {
				e.storeState(state);
			}
		}
	}

	@Override
	public synchronized void restoreState(Hashtable<?, ?> state) {
		for (int i = 0; i < MAXZ; ++i) {
			final Set<VisualElement> setI = (Set<VisualElement>) state.get(hash
					+ "elements" + i);
			if (setI != null) {
				for (final VisualElement e : elements.get(i)) {
					if (setI.contains(e) && elementsToRemove.contains(e)) {
						elementsToRemove.remove(e);
					} else if (!setI.contains(e)
							&& !elementsToRemove.contains(e)) {
						remove(e);
					}
				}

				for (final VisualElement e : setI) {
					if (!elements.get(i).contains(e)) {
						add(e, e.getZDepth());
					}
				}
			}
		}

		for (final Set<VisualElement> set : elements) {
			for (final VisualElement e : set) {
				e.restoreState(state);
			}
		}
	}

	public synchronized void clear() {
		for (final VisualElement e : elementsToRemove) {
			elementsToRemove.remove(e);
		}
		for (final Set<VisualElement> set : elements) {
			set.clear();
		}
	}
}
