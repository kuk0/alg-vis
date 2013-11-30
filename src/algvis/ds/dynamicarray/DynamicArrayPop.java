package algvis.ds.dynamicarray;

import algvis.core.Algorithm;

public class DynamicArrayPop extends Algorithm {
  DynamicArray D;

  DynamicArrayPop(DynamicArray D) {
    super(D.panel, null);
    this.D = D;
  }
  @Override
  public void runAlgorithm() throws InterruptedException {
    D.array.pop();
  }
}
