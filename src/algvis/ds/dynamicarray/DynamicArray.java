package algvis.ds.dynamicarray;

import algvis.core.Array;
import algvis.core.ArrayNode;
import algvis.core.DataStructure;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.VisPanel;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

public class DynamicArray extends DataStructure implements ClickListener {
  public static final String dsName = "dynamicarray";
  public static final String adtName = "dynamicarray";

  Array<ArrayNode> array, newarray;
  ArrayList<DynamicArrayCoin> coins;
  int size = 0;
  int capacity = 2;

  DynamicArray(VisPanel visPanel) {
    super(visPanel);

    panel.screen.V.setDS(this);

    array = new Array<>(0,-200,0);
    array.add(new ArrayNode(this, 0));
    array.add(new ArrayNode(this, 0));

    coins = new ArrayList<>();
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
    start(new DynamicArrayInsert(this, x));
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
    if(newarray != null) newarray.draw(v);
    for(DynamicArrayCoin coin: coins) coin.draw(v);
  }

  @Override
  public void move() {
    array.move();
    for(DynamicArrayCoin coin: coins) coin.move();
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
