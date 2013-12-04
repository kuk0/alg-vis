package algvis.ds.dynamicarray;

import algvis.core.Array;
import algvis.core.ArrayNode;
import algvis.core.DataStructure;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.VisPanel;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

public class DynamicArray extends DataStructure implements ClickListener {
  public static final String dsName = "dynamicarray";
  public static final String adtName = "dynamicarray";

  Array<ArrayNode> array;

  DynamicArray(VisPanel visPanel) {
    super(visPanel);

    panel.screen.V.setDS(this);

    array = new Array<>(0,150,150);
  }

  @Override
  public void mouseClicked(int x, int y) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public String getName() {
    return dsName;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public String stats() {
    return "";  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void insert(int x) {
    start(new DynamicArrayInsert(this, new ArrayNode(this, x, 1)));
  }

  public void pop() {
    start(new DynamicArrayPop(this));
  }

  @Override
  public void clear() {

  }

  @Override
  public void draw(View v) {
    array.draw(v);
  }

  @Override
  protected void move() throws ConcurrentModificationException {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Rectangle2D getBoundingBox() {
    return new Rectangle2D.Double(0,0,0,0);
  }

  @Override
  public void storeState(Hashtable<Object, Object> state) {
    super.storeState(state);
    HashtableStoreSupport.store(state, hash + "array", array);
    this.array.storeState(state);
  }

  @Override
  public void restoreState(Hashtable<?, ?> state) {
    super.restoreState(state);
    final Object array = state.get(hash + "array");
    if (array != null) {
      this.array = (Array) HashtableStoreSupport.restore(array);
    }
    if (this.array != null) {
      this.array.restoreState(state);
    }
  }
}
