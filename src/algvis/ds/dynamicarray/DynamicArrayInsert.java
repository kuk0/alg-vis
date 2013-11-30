package algvis.ds.dynamicarray;

import algvis.core.Algorithm;
import algvis.core.ArrayNode;

public class DynamicArrayInsert extends Algorithm {
  DynamicArray D;
  ArrayNode x;

  DynamicArrayInsert(DynamicArray D, ArrayNode x) {
    super(D.panel, null);
    this.D = D;
    this.x = x;
  }
  @Override
  public void runAlgorithm() throws InterruptedException {
    D.array.add(x);
  }
}
