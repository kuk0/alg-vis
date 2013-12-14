package algvis.ds.dynamicarray;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.ui.view.REL;
import algvis.core.NodeColor;

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
      addStep((Node)D.array.get(0), REL.TOP, "dynamicarray-empty");
    }
    else {
      D.size--;
      D.array.get(D.size).setKey(Node.NOKEY);
      if(D.size != 0 && D.size * 4 <= D.capacity){
        for(int i= D.capacity/2; i < D.capacity; i++) {
          D.array.get(i).setColor(NodeColor.RED);
        }
        createNewArray(D.capacity / 2);
      }
    }
  }
}
