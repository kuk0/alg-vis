package algvis.ds.dynamicarray;

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.ui.view.REL;

public class DynamicArrayPop extends DynamicArrayAlg {
    DynamicArray D;

    DynamicArrayPop(DynamicArray D) {
        super(D);
        this.D = D;
    }

    @Override
    public void runAlgorithm() {
        D.newCoins.clear();
        setHeader("pop");
        for (int i = 0; i < 3; i++) {
            D.newCoins.add(new DynamicArrayCoin(D, D.invisible,
                (int) (i * Node.RADIUS * 2.25), 0));
        }

        if (D.size == 0) {
            addStep(D.array.get(0), REL.TOP, "dynamicarray-empty");
        } else {
            D.newCoins.get(2).setColor(NodeColor.RED);
            addStep(D.array.get(D.size - 1), REL.TOPRIGHT, "dynamicarray-pop");
            pause();

            D.newCoins.get(2).setState(Node.UP);
            D.size--;
            D.array.get(D.size).setKey(Node.NOKEY);
            if (D.coinsForCopy.get(D.size).state != Node.INVISIBLE) {
                D.coinsForCopy.get(D.size).setState(Node.DOWN);
            }
            if (D.coinsForArray.get(D.size).state != Node.INVISIBLE) {
                D.coinsForArray.get(D.size).setState(Node.UP);
            }

            boolean needless = true;

            if (D.size >= D.capacity / 4 && D.size < D.capacity / 2) {
                if (D.coinsForArray
                    .get(D.size - D.capacity / 4).state == Node.INVISIBLE) {
                    needless = false;
                    D.newCoins.get(1).setColor(NodeColor.GREEN);
                    D.newCoins.get(0).setColor(NodeColor.GREEN);
                    D.newCoins.get(1)
                        .changeRelative(D.array.get(D.size - D.capacity / 4));
                    D.newCoins.get(0).moveTo(0, -D.coinsDist);
                    D.newCoins.get(0)
                        .changeRelative(D.array.get(D.size - D.capacity / 4));
                    D.newCoins.get(1).moveTo(0, D.coinsDist);
                    D.coinsForArray.set(D.size - D.capacity / 4,
                        D.newCoins.get(0));
                    D.coinsForCopy.set(D.size - D.capacity / 4,
                        D.newCoins.get(1));
                }
            }

            if (needless) {
                if (D.size != 0) {
                    addStep(D.newCoins.get(0), REL.TOP,
                        "dynamicarray-needless-second");
                } else {
                    addStep(D.newCoins.get(0), REL.TOP,
                        "dynamicarray-needless-empty");
                }
                D.newCoins.get(1).setColor(NodeColor.RED);
                D.newCoins.get(0).setColor(NodeColor.RED);
                pause();
                D.newCoins.get(0).setState(Node.UP);
                D.newCoins.get(1).setState(Node.UP);
            }

            if (D.size != 0 && D.size * 4 <= D.capacity) {
                addStep(D.delimiter4, REL.TOP, "dynamicarray-small");
                pause();
                for (int i = D.capacity / 2; i < D.capacity; i++) {
                    D.array.get(i).setColor(NodeColor.RED);
                }
                createNewArray(D.capacity / 2);
                needless = true;
            }
        }
    }
}
