package algvis.core;

import java.util.Hashtable;

import algvis.core.history.HashtableStoreSupport;

public class RelativeNode extends Node {
    private Node relative;

    public int sepx = 0, sepy = 30;
    public int sepTox = 0, sepToy = 30;

    protected RelativeNode(DataStructure D, Node relative, int key, int sepx,
        int sepy, int zDepth) {
        super(D, key, relative.x + sepx, relative.y + sepy, zDepth);
        this.relative = relative;
        moveTo(sepx, sepy);
    }

    protected RelativeNode(DataStructure D, Node relative, int key,
        int zDepth) {
        this(D, relative, key, 0, 30, zDepth);
    }

    public RelativeNode(Node relative, Node v) {
        super(v);
        changeRelative(relative);
    }

    @Override
    public void move() {
        switch (state) {
        case Node.ALIVE:
        case Node.INVISIBLE:
            tox = relative.x + sepx;
            toy = relative.y + sepy;
            if (steps > 0) {
                sepx += (sepTox - sepx) / steps;
                sepy += (sepToy - sepy) / steps;
                x += (tox - x) / steps;
                y += (toy - y) / steps;
                steps--;
            }
            if (steps == 0 && (tox != x || toy != y)) {
                x = tox;
                y = toy;
            }
            break;
        case Node.DOWN:
        case Node.LEFT:
        case Node.RIGHT:
        case Node.UP:
            if (state == Node.DOWN) {
                y += 20;
            }
            if (state == Node.UP) {
                y -= 20;
            }
            if (state == Node.LEFT) {
                x -= 20;
            } else if (state == Node.RIGHT) {
                x += 20;
            }
            // robi problem, ked rychlo dozadu a potom rychlo dopredu
            if (!D.panel.screen.V.inside(x, y - Node.RADIUS)) {
                state = OUT;
            }
            break;
        }
    }

    public void moveToRelative() {
        this.x = this.tox = relative.x + sepx;
        this.y = this.toy = relative.y + sepy;
    }

    public void goToRelative(int septox, int septoy) {
        this.sepTox = septox;
        this.sepToy = septoy;
        this.steps = 10;
    }

    public void moveTo(int sepx, int sepy) {
        this.sepx = this.sepTox = sepx;
        this.sepy = this.sepToy = sepy;
    }

    public void changeRelative(Node v) {
        relative = v;
        this.steps = 10;
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "sepx", sepx);
        HashtableStoreSupport.store(state, hash + "sepy", sepy);
        HashtableStoreSupport.store(state, hash + "sepTox", sepTox);
        HashtableStoreSupport.store(state, hash + "sepToy", sepToy);
        HashtableStoreSupport.store(state, hash + "relative", relative);
        relative.storeState(state);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object sepx = state.get(hash + "sepx");
        if (sepx != null) {
            this.sepx = (int) HashtableStoreSupport.restore(sepx);
        }

        final Object sepy = state.get(hash + "sepy");
        if (sepy != null) {
            this.sepy = (int) HashtableStoreSupport.restore(sepy);
        }

        final Object sepTox = state.get(hash + "sepTox");
        if (sepTox != null) {
            this.sepTox = (int) HashtableStoreSupport.restore(sepTox);
        }

        final Object sepToy = state.get(hash + "sepToy");
        if (sepToy != null) {
            this.sepToy = (int) HashtableStoreSupport.restore(sepToy);
        }

        final Object relative = state.get(hash + "relative");
        if (relative != null) {
            this.relative = (Node) HashtableStoreSupport.restore(relative);
        }
        this.relative.restoreState(state);
    }
}
