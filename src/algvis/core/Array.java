package algvis.core;

import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.VisualElement;
import algvis.ui.view.View;

import javax.swing.undo.StateEditable;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

public class Array<E extends ArrayNode> extends VisualElement implements StateEditable {
  public ArrayList<E> array = new ArrayList<>();

  public volatile int x;
  public volatile int y;
  public int tox,toy;

  protected int steps;
  /** the state of a node - either ALIVE, DOWN, LEFT, or RIGHT. */
  public int state = Node.ALIVE;
  private NodeColor color = NodeColor.NORMAL;

  public static final int STEPS = 10;
  public static final int RADIUS = 10;

  public Array(int zDepth, int x, int y) {
    super(zDepth);
    this.x = this.tox = x;
    this.y = this.toy = y;
  }

  public int getSize() {
    return array.size();
  }

  public E get(int i) {
    return array.get(i);
  }

  public void set(int pos, E N) {
    array.add(pos, N);
  }

  public void add(E N) {
    array.add(N);
  }

  public void pop() {
    array.remove(array.size() - 1);
  }

  @Override
  public void draw(View v) throws ConcurrentModificationException {
    int lastx = x;
    for(ArrayNode N : array) {
      N.x = lastx + Node.RADIUS;
      N.y = y;
      N.draw(v);
      lastx = (int) N.getBoundingBox().getMaxX();
    }
  }

  @Override
  public void move() throws ConcurrentModificationException {
    if (steps > 0) {
      x += (tox - x) / steps;
      y += (toy - y) / steps;
      --steps;
    }
  }

  public void moveTo(int x,int y) {
    this.tox = x;
    this.toy = y;
    this.steps = STEPS;
  }

  @Override
  public Rectangle2D getBoundingBox() {
    if(array.isEmpty()) return new Rectangle2D.Double(x,y,0,0);

    Rectangle2D rec = array.get(0).getBoundingBox();
    for(ArrayNode N: array) rec.createUnion(N.getBoundingBox());
    return rec;
  }

  @Override
  public void storeState(Hashtable<Object, Object> state) {
    super.storeState(state);
    HashtableStoreSupport.store(state, hash + "x", x);
    HashtableStoreSupport.store(state, hash + "y", x);
    HashtableStoreSupport.store(state, hash + "tox", tox);
    HashtableStoreSupport.store(state, hash + "toy", toy);
    HashtableStoreSupport.store(state, hash + "steps", steps);
    HashtableStoreSupport.store(state, hash + "size", array.size());
    for(int i = 0; i < array.size(); i++) {
      HashtableStoreSupport.store(state, hash + "elem" +new Integer(i).toString(), array.get(i));
    }

    for(int i = 0; i < array.size(); i++) {
      array.get(i).storeState(state);
    }
  }

  @Override
  public void restoreState(Hashtable<?, ?> state) {
    super.restoreState(state);

    final Object x = state.get(hash + "x");
    if (x != null) {
      this.x = (int) HashtableStoreSupport.restore(x);
    }

    final Object y = state.get(hash + "y");
    if (y != null) {
      this.y = (int) HashtableStoreSupport.restore(y);
    }

    final Object tox = state.get(hash + "tox");
    if (tox != null) {
      this.tox = (int) HashtableStoreSupport.restore(tox);
    }

    final Object toy = state.get(hash + "toy");
    if (toy != null) {
      this.toy = (int) HashtableStoreSupport.restore(toy);
    }

    final Object steps = state.get(hash + "steps");
    if (steps != null) {
      this.steps = (int) HashtableStoreSupport.restore(steps);
    }

    final Object size = state.get(hash + "size");
    if (size != null) {
      int sz = (int) HashtableStoreSupport.restore(size);
      Object elem = state.get(hash  + "elem" + new Integer(sz-1).toString());
      if(elem != null) {
        array.add((E) elem);
        ((ArrayNode) elem).restoreState(state);
      }
      while(sz < array.size()) array.remove(array.size()-1);
    }
  }
}
