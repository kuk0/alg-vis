package algvis.ds.dynamicarray;

import algvis.core.*;
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
  ArrayList<DynamicArrayCoin> coinsForCopy, coinsForArray, newCoins;
  Node invisible;

  int size = 0;
  int capacity = 2;
  int coinsDist = 60;
  DynamicArrayDelimiter delimiter2, delimiter4, newdelimiter2, newdelimiter4;


  DynamicArray(VisPanel visPanel) {
    super(visPanel);

    panel.screen.V.setDS(this);

    array = new Array<>(0,-200,100);
    array.add(new ArrayNode(this, 0));
    array.add(new ArrayNode(this, 0));

    coinsForCopy = new ArrayList<>();
    coinsForArray = new ArrayList<>();
    newCoins = new ArrayList<>();

    for(int i=0; i < capacity; i++) {
      coinsForArray.add(new DynamicArrayCoin(array.get(i), this, -20, 0));
      coinsForArray.get(i).setState(Node.INVISIBLE);
      coinsForArray.get(i).setColor(NodeColor.GREEN);
      coinsForCopy.add(new DynamicArrayCoin(array.get(i), this, 20, 0));
      coinsForCopy.get(i).setState(Node.INVISIBLE);
      coinsForArray.get(i).setColor(NodeColor.GREEN);
    }

    invisible = new ArrayNode(this, 0);
    invisible.x = invisible.tox = -180;
    invisible.y = invisible.toy = 0 ;
    invisible.setState(Node.INVISIBLE);
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
    for(DynamicArrayCoin coin: coinsForCopy) coin.draw(v);
    for(DynamicArrayCoin coin: coinsForArray) coin.draw(v);
    for(DynamicArrayCoin coin: newCoins) coin.draw(v);


    if(delimiter2 != null) delimiter2.draw(v);
    if(delimiter4 != null) delimiter4.draw(v);
    if(newdelimiter2 != null) newdelimiter2.draw(v);
    if(newdelimiter4 != null) newdelimiter4.draw(v);
  }

  @Override
  public void move() {
    array.move();
    for(DynamicArrayCoin coin: coinsForCopy) coin.move();
    for(DynamicArrayCoin coin: coinsForArray) coin.move();
    for(DynamicArrayCoin coin: newCoins) coin.move();

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
