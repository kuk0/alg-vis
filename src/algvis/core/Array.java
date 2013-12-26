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
    N.x = N.tox = this.x + array.size() * E.RADIUS*2;
    N.y = N.toy = this.y;
  }

  public void add(E N) {
    array.add(N);
    N.x = this.x + array.size() * Node.RADIUS*2;
    N.y = this.y;
  }

  public void pop() {
    array.get(array.size() - 1).goDown();
    array.remove(array.size() - 1);
  }

  @Override
  public void draw(View v) throws ConcurrentModificationException {
    int lastx = x;
    for(ArrayNode N : array) {
      N.tox = lastx + Node.RADIUS;
      N.toy = y;
      N.steps = Node.STEPS;
      N.draw(v);
      lastx = lastx + 2 * Node.RADIUS;
    }
  }

  @Override
  public void move() throws ConcurrentModificationException {
    for(ArrayNode N : array) {
      N.move();
    }
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
    HashtableStoreSupport.store(state, hash + "y", y);
    HashtableStoreSupport.store(state, hash + "tox", tox);
    HashtableStoreSupport.store(state, hash + "toy", toy);
    HashtableStoreSupport.store(state, hash + "steps", steps);
    HashtableStoreSupport.store(state, hash + "array", array.clone());
    for(E node: array) node.storeState(state);
  }

  @Override
  public void restoreState(Hashtable<?, ?> state) {
    super.restoreState(state);

    final Object x = state.get(hash + "x");
    if (x != null) {
      this.x = (Integer) HashtableStoreSupport.restore(x);
    }

    final Object y = state.get(hash + "y");
    if (y != null) {
      this.y = (Integer) HashtableStoreSupport.restore(y);
    }

    final Object tox = state.get(hash + "tox");
    if (tox != null) {
      this.tox = (Integer) HashtableStoreSupport.restore(tox);
    }

    final Object toy = state.get(hash + "toy");
    if (toy != null) {
      this.toy = (Integer) HashtableStoreSupport.restore(toy);
    }

    final Object steps = state.get(hash + "steps");
    if (steps != null) {
      this.steps = (Integer) HashtableStoreSupport.restore(steps);
    }

    final Object array = state.get(hash + "array");
    if (array != null) {
      this.array = (ArrayList<E>) HashtableStoreSupport.restore(array);
    }
    for(E node: this.array) node.restoreState(state);
  }
}
