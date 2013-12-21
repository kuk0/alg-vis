package algvis.ds.dynamicarray;

import algvis.core.*;
import algvis.ui.view.REL;

import java.awt.*;

abstract class DynamicArrayAlg extends Algorithm {
  DynamicArray D;

  public DynamicArrayAlg(DynamicArray D) {
    super(D.panel);
    this.D = D;
  }

  public void createNewArray(int capacity) throws InterruptedException {
    for(DynamicArrayCoin coin : D.coinsForArray) coin.setColor(NodeColor.RED);
    addStep((Node)D.coinsForArray.get(D.coinsForArray.size()/2), REL.TOP, "dynamicarray-use-coin");
    pause();
    for(DynamicArrayCoin coin : D.coinsForArray) coin.setState(Node.UP);
    D.newarray = new Array<>(0,D.array.tox, D.array.toy + 150);
    D.capacity = capacity;
    for(int i=0; i < D.capacity; i++) {
      D.newarray.add(new ArrayNode(D, 0));
    }
    addStep((Node)D.newarray.get(0), REL.TOPRIGHT, "dynamicarray-new");

    if(capacity >= 4) {
      D.newdelimiter4 = new DynamicArrayDelimiter(D.newarray.get(capacity/4), D, Color.GREEN );
      D.newdelimiter2 = new DynamicArrayDelimiter(D.newarray.get(capacity/2), D, Color.RED );
    }
    pause();
    if(D.size > 0) {
      addStep((Node)D.newarray.get(0), REL.TOPRIGHT, "dynamicarray-copy");
    }
    for(int i=0;i < D.size; i++) {
      D.coinsForCopy.get(i).setColor(NodeColor.RED);
      D.newarray.get(i).setKey(D.array.get(i).getKey());
      D.array.get(i).setKey(Node.NOKEY);
      pause();
      D.coinsForCopy.get(i).setState(Node.UP);
    }
    addStep(D.array.get(0), REL.TOPRIGHT, "dynamicarray-erase");
    pause();

    D.array = null;
    D.newarray.moveTo(D.newarray.x, D.newarray.y - 150);
    D.array = D.newarray;
    D.newarray = null;
    D.delimiter4 = D.newdelimiter4;
    D.newdelimiter4 = null;
    D.delimiter2 = D.newdelimiter2;
    D.newdelimiter2 = null;

    D.coinsForArray.clear();
    D.coinsForCopy.clear();
    for(int i=0; i < capacity; i++) {
      D.coinsForArray.add(new DynamicArrayCoin(D.invisible, D, 0, 0));
      D.coinsForArray.get(i).setState(Node.INVISIBLE);
      D.coinsForCopy.add(new DynamicArrayCoin(D.invisible, D, 0, 0));
      D.coinsForCopy.get(i).setState(Node.INVISIBLE);
    }
    return;
  }
}
