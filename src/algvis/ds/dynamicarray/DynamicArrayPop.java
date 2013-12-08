package algvis.ds.dynamicarray;

import algvis.core.Algorithm;
import algvis.core.Node;

public class DynamicArrayPop extends DynamicArrayAlg {
  DynamicArray D;

  DynamicArrayPop(DynamicArray D) {
    super(D);
    this.D = D;
  }
  @Override
  public void runAlgorithm() throws InterruptedException {
    D.array.get(D.size).setKey(Node.NOKEY);
    D.size--;
    if(D.size != 0 && D.size * 4 <= D.capacity)
      createNewArray(D.capacity / 2);
  }
}
