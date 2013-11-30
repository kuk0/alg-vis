package algvis.core;

import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.VisualElement;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

public class ArrayNode extends VisualElement {
  private String value;
  private boolean hasValue;

  public volatile int x;
  public volatile int y;
  public int tox;
  public int toy;
  protected int steps = 0;

  public static final int STEPS = 10;
  public static final int SIZE = 10 ;

  public ArrayNode(String value,int zDepth) {
    super(zDepth);
    hasValue = true;
    this.value = value;
  }

  public ArrayNode(int zDepth) {
    super(zDepth);
    hasValue = false;
  }
  @Override
  protected void draw(View v) throws ConcurrentModificationException {
    v.setColor(Color.YELLOW);
    v.fillSqr(x, y, ArrayNode.SIZE);

    v.setColor(Color.BLACK);
    v.drawSquare(x,y, ArrayNode.SIZE);

    if(hasValue) {
      v.setColor(Color.BLACK);
      v.drawString(value, x, y, Fonts.NORMAL );
    }
  }

  public void moveTo(int x, int y) {
    this.x = x; this.y = y;
  }

  public void goTo(int tox, int toy) {
    this.tox = tox;
    this.toy = toy;
    this.steps = STEPS;
  }

  @Override
  protected void move() throws ConcurrentModificationException {
    if(steps > 0) {
      x += (x - tox)/steps;
      y += (y - toy)/steps;
      steps--;
    }
  }

  @Override
  protected Rectangle2D getBoundingBox() {
    return new Rectangle2D.Double(x, y, ArrayNode.SIZE, ArrayNode.SIZE);
  }

  @Override
  public void storeState(Hashtable<Object, Object> state) {
    super.storeState(state);
    HashtableStoreSupport.store(state, hash + "value", value);
    HashtableStoreSupport.store(state, hash + "hasValue", hasValue);
    HashtableStoreSupport.store(state, hash + "x", x);
    HashtableStoreSupport.store(state, hash + "y", x);
    HashtableStoreSupport.store(state, hash + "tox", tox);
    HashtableStoreSupport.store(state, hash + "toy", toy);
    HashtableStoreSupport.store(state, hash + "steps", steps);
  }

  @Override
  public void restoreState(Hashtable<?, ?> state) {
    super.restoreState(state);

    final Object value = state.get(hash + "value");
    if (value != null) {
      this.value = (String) HashtableStoreSupport.restore(value);
    }

    final Object hasValue= state.get(hash + "hasValue");
    if (hasValue != null) {
      this.hasValue = (Boolean) HashtableStoreSupport.restore(hasValue);
    }

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
  }
}
