package algvis.ds.dynamicarray;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.ui.view.REL;

public class DynamicArrayPop extends DynamicArrayAlg {
  DynamicArray D;

  DynamicArrayPop(DynamicArray D) {
    super(D);
    this.D = D;
  }
  @Override
  public void runAlgorithm() throws InterruptedException {
    setHeader("pop");
    if(D.size == 0) {
      addStep((Node)D.array.get(0), REL.BOTTOM, "dynamicarray-empty");
    }
    else {
      D.array.get(D.size-1).setKey(Node.NOKEY);
      D.size--;
      if(D.size != 0 && D.size * 4 <= D.capacity)
       createNewArray(D.capacity / 2);
    }
  }
}
