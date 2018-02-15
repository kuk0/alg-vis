package algvis.ds.dynamicarray;

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.ui.view.REL;

public class DynamicArrayInsert extends DynamicArrayAlg {
  int x;

  DynamicArrayInsert(DynamicArray D, int x) {
    super(D);
    this.x = x;
  }
  @Override
  public void runAlgorithm() {
    D.newCoins.clear();
    setHeader("insert");
    for(int i=0; i < 5; i++) {
      D.newCoins.add(new DynamicArrayCoin(D, D.invisible, (int) (i * Node.RADIUS * 2.25), 0));
    } 
    if(D.size + 1 > D.capacity) {
      addStep(D.array.get(D.capacity-1), REL.TOPRIGHT, "dynamicarray-full");
      pause();
      createNewArray(D.capacity * 2);
    }

    addStep(D.newCoins.get(4), REL.TOP, "dynamicarray-insert");
    D.newCoins.get(4).setColor(NodeColor.RED);
    pause();
    D.newCoins.get(4).setState(Node.UP);
    D.array.get(D.size).setKey(x);
    D.size++;
    pause();
    addStep(D.newCoins.get(2), REL.TOP, "dynamicarray-insert-coin");
    pause();

    if(D.size > D.capacity/2) {
      D.newCoins.get(3).setColor(NodeColor.RED);
      D.newCoins.get(2).setColor(NodeColor.RED);
      pause();
      D.coinsForCopy.set(D.size - 1, D.newCoins.get(3));
      D.coinsForArray.set(D.size - 1, D.newCoins.get(2));

      D.newCoins.get(3).setColor(NodeColor.GREEN);
      D.newCoins.get(2).setColor(NodeColor.GREEN);

      D.newCoins.get(3).changeRelative(D.array.get(D.size-1)); D.newCoins.get(3).moveTo(0, D.coinsDist);
      D.newCoins.get(2).changeRelative(D.array.get(D.size-1)); D.newCoins.get(2).moveTo(0, -D.coinsDist);

      addStep(D.newCoins.get(0), REL.TOP, "dynamicarray-insert-coin");
      D.newCoins.get(1).setColor(NodeColor.RED);
      D.newCoins.get(0).setColor(NodeColor.RED);
      pause();

      int tween = D.capacity / 2 - (D.size - D.capacity / 2);
      boolean stop0 = false, stop1 = false;
      if(D.coinsForCopy.get(tween).state == Node.INVISIBLE) {
        D.newCoins.get(1).setColor(NodeColor.GREEN);
        D.newCoins.get(1).changeRelative(D.array.get(tween)); D.newCoins.get(1).moveTo(0, D.coinsDist);
        D.coinsForCopy.set(tween, D.newCoins.get(1));
      }
      else {
        addStep(D.coinsForCopy.get(tween), REL.BOTTOM, "dynamicarray-insert-enough");
        stop1= true;
      }

      if(D.coinsForArray.get(tween).state == Node.INVISIBLE) {
        D.newCoins.get(0).changeRelative(D.array.get(tween)); D.newCoins.get(0).moveTo(0, -D.coinsDist);
        D.newCoins.get(0).setColor(NodeColor.GREEN);
        D.coinsForArray.set(tween, D.newCoins.get(0));
      }
      else {
        stop0 = true;
        addStep(D.coinsForArray.get(tween), REL.TOP, "dynamicarray-insert-enough");
      }
      if(stop0 || stop1) pause();
      if(stop0) D.newCoins.get(0).setState(Node.UP);
      if(stop1) D.newCoins.get(1).setState(Node.UP);
    }
    else {
      for(DynamicArrayCoin coin: D.newCoins) coin.setColor(NodeColor.RED);
      addStep(D.newCoins.get(0), REL.TOP, "dynamicarray-needless-first");
      pause();
      for(DynamicArrayCoin coin: D.newCoins) coin.setState(Node.UP);
    }
  }
}
