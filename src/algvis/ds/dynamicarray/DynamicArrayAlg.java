package algvis.ds.dynamicarray;

import algvis.core.*;

import java.awt.*;

abstract class DynamicArrayAlg extends Algorithm {
  DynamicArray D;

  public DynamicArrayAlg(DynamicArray D) {
    super(D.panel);
    this.D = D;
  }

  public void createNewArray(int capacity) throws InterruptedException {
    D.newarray = new Array<>(0,D.array.x, D.array.y+150);

    D.capacity = capacity;
    for(int i=0; i < D.capacity; i++) {
      D.newarray.add(new ArrayNode(D, 0));
    }
    D.newdelimiter4 = new DynamicArrayDelimiter(D.newarray.get(capacity/4), D, Color.GREEN );
    D.newdelimiter2 = new DynamicArrayDelimiter(D.newarray.get(capacity/2), D, Color.RED );
    pause();
    for(int i=0;i < D.size; i++) {
      D.newarray.get(i).setKey(D.array.get(i).getKey());
      D.array.get(i).setKey(Node.NOKEY);
      pause();
    }

    D.array = null;
    D.newarray.moveTo(D.newarray.x, D.newarray.y - 150);
    D.array = D.newarray;
    D.newarray = null;
    D.delimiter4 = D.newdelimiter4;
    D.newdelimiter4 = null;
    D.delimiter2 = D.newdelimiter2;
    D.newdelimiter2 = null;
    return;
  }
}
