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
  DynamicArrayDelimiter delimiter2, delimiter4, newdelimiter2, newdelimiter4;


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
  }

  @Override
  public String getName() {
    return dsName;
  }

  @Override
  public String stats() {
    return "";
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

    if(delimiter2 != null) delimiter2.draw(v);
    if(delimiter4 != null) delimiter4.draw(v);
    if(newdelimiter2 != null) newdelimiter2.draw(v);
    if(newdelimiter4 != null) newdelimiter4.draw(v);
  }

  @Override
  public void move() {
    array.move();
    for(DynamicArrayCoin coin: coins) coin.move();

    if(delimiter2 != null) delimiter2.move();
    if(delimiter4 != null) delimiter4.move();
    if(newdelimiter2 != null) newdelimiter2.move();
    if(newdelimiter4 != null) newdelimiter2.move();
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
