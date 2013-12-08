package algvis.ds.dynamicarray;

import algvis.core.Algorithm;
import algvis.core.Array;
import algvis.core.ArrayNode;
import algvis.core.Node;

public class DynamicArrayInsert extends DynamicArrayAlg {
  int x;

  DynamicArrayInsert(DynamicArray D, int x) {
    super(D);
    this.x = x;
  }
  @Override
  public void runAlgorithm() throws InterruptedException {
    if(D.size + 1 > D.capacity) createNewArray(D.capacity * 2);

    D.array.get(D.size).setKey(x);
    D.size++;
  }
}
