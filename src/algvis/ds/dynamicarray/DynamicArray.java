package algvis.ds.dynamicarray;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;

import algvis.core.Array;
import algvis.core.ArrayNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.history.HashtableStoreSupport;
import algvis.internationalization.IString;
import algvis.internationalization.Languages;
import algvis.ui.VisPanel;
import algvis.ui.view.ClickListener;
import algvis.ui.view.REL;
import algvis.ui.view.View;

public class DynamicArray extends DataStructure implements ClickListener {
  public static final String dsName = "dynamicarray";

  Array<ArrayNode> array, newarray;
  ArrayList<DynamicArrayCoin> coinsForCopy, coinsForArray, newCoins;
  Node invisible;

  int size = 0;
  int capacity = 2;
  final int coinsDist = 40;
  DynamicArrayDelimiter delimiter2, delimiter4, newdelimiter2, newdelimiter4;



  DynamicArray(VisPanel visPanel) {
    super(visPanel);

    panel.screen.V.setDS(this);

    clear();

    invisible = new ArrayNode(this, 0);
    invisible.x = invisible.tox = -200;
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
    return Languages.getString("size") + ": " + size + ", " +
        Languages.getString("capacity") + ": " + capacity;
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
    size = 0;
    capacity = 2;

    newarray = array = new Array<>(0,-250,100);
    array.add(new ArrayNode(this, 0));
    array.add(new ArrayNode(this, 0));

    newdelimiter2 = delimiter2 = new DynamicArrayDelimiter(this, array.get(1), Color.GREEN);
    newdelimiter4 = delimiter4 = new DynamicArrayDelimiter(this, array.get(1), Color.GREEN);
    delimiter4.setState(Node.INVISIBLE);

    coinsForCopy = new ArrayList<>();
    coinsForArray = new ArrayList<>();
    newCoins = new ArrayList<>();

    for(int i=0; i < capacity; i++) {
      coinsForArray.add(new DynamicArrayCoin(this, array.get(i), -20, 0));
      coinsForArray.get(i).setState(Node.INVISIBLE);
      coinsForArray.get(i).setColor(NodeColor.GREEN);
      coinsForCopy.add(new DynamicArrayCoin(this, array.get(i), 20, 0));
      coinsForCopy.get(i).setState(Node.INVISIBLE);
      coinsForArray.get(i).setColor(NodeColor.GREEN);
    }
  }

  @Override
  public void draw(View v) {
    array.draw(v);
    if(newarray != null && newarray != array) newarray.draw(v);
    for(DynamicArrayCoin coin: coinsForCopy) coin.draw(v);
    for(DynamicArrayCoin coin: coinsForArray) coin.draw(v);
    for(DynamicArrayCoin coin: newCoins) coin.draw(v);

    if(delimiter2 != null) delimiter2.draw(v);
    if(delimiter4 != null) delimiter4.draw(v);
    if(newdelimiter2 != null && newdelimiter2 != delimiter2) newdelimiter2.draw(v);
    if(newdelimiter4 != null && newdelimiter4 != delimiter4) newdelimiter4.draw(v);


    v.drawTextBubble(new IString("dynamicarray-copyText").getString(), -250, 140, 75, 255, REL.LEFT);
    v.drawTextBubble(new IString("dynamicarray-allocateText").getString(), -250, 60, 75, 255, REL.LEFT);
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
    if(newdelimiter4 != null) newdelimiter4.move();
  }

  @Override
  public Rectangle2D getBoundingBox() {
    return new Rectangle2D.Double(0,0,0,0);
  }

  @Override
  public void storeState(Hashtable<Object, Object> state) {
    super.storeState(state);
    HashtableStoreSupport.store(state, hash + "array", array);
    HashtableStoreSupport.store(state, hash + "newarray", newarray);
    HashtableStoreSupport.store(state, hash + "size", size);
    HashtableStoreSupport.store(state, hash + "capacity", capacity);
    HashtableStoreSupport.store(state, hash + "coinsForArray", coinsForArray.clone());
    HashtableStoreSupport.store(state, hash + "coinsForCopy", coinsForCopy.clone());
    HashtableStoreSupport.store(state, hash + "newCoins",newCoins.clone());
    HashtableStoreSupport.store(state, hash + "delimiter2", delimiter2);
    HashtableStoreSupport.store(state, hash + "delimiter4", delimiter4);
    HashtableStoreSupport.store(state, hash + "newdelimiter2", newdelimiter2);
    HashtableStoreSupport.store(state, hash + "newdelimiter4", newdelimiter4);
    HashtableStoreSupport.store(state, hash + "invisible", invisible);

    this.array.storeState(state);
    if(this.newarray != null)this.newarray.storeState(state);
    for(DynamicArrayCoin coin: coinsForArray) coin.storeState(state);
    for(DynamicArrayCoin coin: coinsForCopy) coin.storeState(state);
    for(DynamicArrayCoin coin: newCoins) coin.storeState(state);

    if(this.delimiter2 != null)delimiter2.storeState(state);
    if(this.delimiter4 != null)delimiter4.storeState(state);
    if(this.newdelimiter2 != null) newdelimiter2.storeState(state);
    if(this.newdelimiter4 != null) newdelimiter4.storeState(state);
    this.invisible.storeState(state);
  }

  @Override
  public void restoreState(Hashtable<?, ?> state) {
    super.restoreState(state);
    final Object array = state.get(hash + "array");
    if (array != null) {
      this.array = (Array<ArrayNode>) HashtableStoreSupport.restore(array);
    }
    if (this.array != null) {
      this.array.restoreState(state);
    }

    final Object newarray = state.get(hash + "newarray");
    if (newarray != null) {
      this.newarray = (Array<ArrayNode>) HashtableStoreSupport.restore(array);
    }
    if (this.newarray != null) {
      this.newarray.restoreState(state);
    }

    final Object size = state.get(hash + "size");
    if(size != null) {
        this.size = (Integer) HashtableStoreSupport.restore(size);
    }
    final Object capacity = state.get(hash + "capacity");
    if(capacity != null) {
        this.capacity = (Integer) HashtableStoreSupport.restore(capacity);
    }
    final Object coinsForCopy = state.get(hash + "coinsForCopy");
    if(coinsForCopy != null) {
        this.coinsForCopy = (ArrayList<DynamicArrayCoin>) HashtableStoreSupport.restore(coinsForCopy);
    }
    for(DynamicArrayCoin coin: this.coinsForCopy) coin.restoreState(state);

    final Object coinsForArray = state.get(hash + "coinsForArray");
    if(coinsForArray != null) {
        this.coinsForArray = (ArrayList<DynamicArrayCoin>) HashtableStoreSupport.restore(coinsForArray);
    }
    for(DynamicArrayCoin coin: this.coinsForArray) coin.restoreState(state);

    final Object newCoins = state.get(hash + "newCoins");
    if(newCoins != null) {
        this.newCoins = (ArrayList<DynamicArrayCoin>) HashtableStoreSupport.restore(newCoins);
    }
    for(DynamicArrayCoin coin: this.newCoins) coin.restoreState(state);

    final Object delimiter2 = state.get(hash + "delimiter2");
    if(delimiter2 != null) {
        this.delimiter2 = (DynamicArrayDelimiter) HashtableStoreSupport.restore(delimiter2);
    }
    if(this.delimiter2 != null)this.delimiter2.restoreState(state);

    final Object delimiter4 = state.get(hash + "delimiter4");
    if(delimiter4 != null) {
        this.delimiter4 = (DynamicArrayDelimiter) HashtableStoreSupport.restore(delimiter4);
    }
    if(this.delimiter4 != null) this.delimiter4.restoreState(state);

    final Object newdelimiter2 = state.get(hash + "newdelimiter2");
    if(newdelimiter2 != null) {
        this.newdelimiter2 = (DynamicArrayDelimiter) HashtableStoreSupport.restore(newdelimiter2);
    }
    if(this.newdelimiter2 != null) this.newdelimiter2.restoreState(state);

    final Object newdelimiter4 = state.get(hash + "newdelimiter4");
    if(newdelimiter4 != null) {
        this.newdelimiter4 = (DynamicArrayDelimiter) HashtableStoreSupport.restore(newdelimiter4);
    }
    if(this.newdelimiter4 != null) this.newdelimiter4.restoreState(state);

    final Object invisible = state.get(hash + "invisible");
    if(invisible != null) {
      this.invisible = (Node) HashtableStoreSupport.restore(invisible);
    }
    if(this.invisible != null) this.invisible.restoreState(state);
  }
}
