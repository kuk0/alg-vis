/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.core;

import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.VisualElement;
import algvis.core.visual.ZDepth;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

/**
 * The Class Node. This is a basic element of the visualization. Nodes can be
 * drawn, they can move, change color, become marked/unmarked, or point in some
 * direction. Nodes are by default drawn as circles with their key in the
 * middle.
 */
public class Node extends VisualElement {
    public DataStructure D;
    protected int key;
    /**
     * x, y - node position tox, toy - the position, where the node is heading
     * steps - the number of steps to reach the destination
     */
    public volatile int x;
    public volatile int y;
    public int tox;
    public int toy;
    protected int steps;
    /** the state of a node - either ALIVE, DOWN, LEFT, or RIGHT. */
    public int state = ALIVE;
    private NodeColor color = NodeColor.NORMAL;
    public boolean marked = false;
    protected Node dir = null;
    protected int arrow = Node.NOARROW; // NOARROW or angle (0=E, 45=SE, 90=S,
                                      // 135=SW, 180=W)
    protected boolean arc = false;

    public static final int STEPS = 10;
    public static final int RADIUS = 10;

    /**
     * the key values are generally integers from 1 to 999 (inclusive) special
     * values of a key are: INF (infinity) or -INF (-infinity), these are drawn
     * nicely as the numeral 8 sideways NOKEY (drawn as an empty circle), and
     * NULL (not drawn at all)
     */
    public static final int INF = 99999, NOKEY = -1, NULL = 100000;
    /**
     * a node can be in several different states: INVISIBLE (default starting
     * state, not drawn), ALIVE (visible), DOWN, LEFT, and RIGHT (the node moves
     * down, or diagonally left or right until it gets out of the screen, and
     * then turns INVISIBLE)
     */
    public static final int INVISIBLE = -1;
    public static final int ALIVE = 0;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int OUT = 5;
    public static final int UP = 6;    
    public static final int NOARROW = -10000;
    public static final int DIRARROW = -10001;
    public static final int TOARROW = -10002;
    public static final int UPY = -7 * Node.RADIUS;

    protected Node(DataStructure D, int key, int x, int y) {
        this(D, key, x, y, ZDepth.NODE);
    }

    protected Node(DataStructure D, int key, int x, int y, int zDepth) {
        super(zDepth);
        this.D = D;
        this.setKey(key);
        this.x = tox = x;
        this.y = toy = y;
        steps = 0;
    }

    protected Node(DataStructure D, int key, int zDepth) {
        this(D, key, 0, UPY, zDepth);
    }

    public Node(Node v) {
        this(v.D, v.getKey(), v.x, v.y);
    }

    public void setState(int s) {
        state = s;
    }

    public NodeColor getColor() {
        return color;
    }

    public void setColor(NodeColor color) {
        fgColor(color.fgColor);
        bgColor(color.bgColor);
        this.color = color;
    }

    protected void fgColor(Color fg) {
        if (fg != color.fgColor) {
            color = new NodeColor(fg, color.bgColor);
        }
    }

    protected void bgColor(Color bg) {
        if (bg != color.bgColor) {
            color = new NodeColor(color.fgColor, bg);
        }
    }

    protected Color getFgColor() {
        return color.fgColor;
    }

    public Color getBgColor() {
        return color.bgColor;
    }

    /**
     * Set background color depending on the key (the higher the key, the darker
     * the color).
     */
    protected void bgKeyColor() {
        bgColor(new Color(255, 255 - getKey() / 20, 0));
    }

    public void mark() {
        marked = true;
    }

    public void unmark() {
        marked = false;
    }

    /**
     * Draw an arrow pointing above the node w.
     * 
     * @param w
     */
    public void pointAbove(Node w) {
        dir = w;
        arrow = Node.DIRARROW;
    }

    /**
     * Draw an arrow pointing to the node w.
     * 
     * @param w
     */
    public void pointTo(Node w) {
        dir = w;
        arrow = Node.TOARROW;
    }

    /**
     * Point in direction angle. The angle in degrees should be a nonnegative
     * integer 0 = RIGHT, then clockwise: 90 = DOWN, 180 = LEFT
     * 
     * @param angle
     */
    public void pointInDir(int angle) {
        dir = null;
        arrow = angle;
    }

    /**
     * Stop drawing an arrow.
     */
    public void noArrow() {
        dir = null;
        arrow = Node.NOARROW;
    }

    /**
     * Draw an arc pointing to node w. Assumption: w is above this node.
     * 
     * @param w
     */
    public void setArc(Node w) {
        dir = w;
        arc = true;
    }

    /**
     * Stop drawing an arc.
     */
    public void noArc() {
        arc = false;
    }

    /**
     * Draw bg.
     * 
     * @param v
     *            view
     */
    protected void drawBg(View v) {
        v.setColor(getBgColor());
        v.fillCircle(x, y, Node.RADIUS);
        v.setColor(Color.BLACK); // fgcolor);
        v.drawCircle(x, y, Node.RADIUS);
        if (marked) {
            v.drawCircle(x, y, Node.RADIUS + 2);
        }
    }

    /**
     * Convert the key into a string (INF is converted to "8" sideways).
     */
    @Override
    public String toString() {
        if (getKey() == INF) {
            return "\u221e";
        } else if (getKey() == -INF) {
            return "-\u221e";
        } else {
            return "" + getKey();
        }
    }

    protected void drawKey(View v) {
        v.setColor(getFgColor());
        if (getKey() != NOKEY) {
            v.drawString(toString(), x, y, Fonts.NORMAL);
        }
    }

    protected void drawArrow(View v) {
        if (arrow == Node.NOARROW || (arrow < 0 && dir == null)) {
            return;
        }
        double dx, dy;
        if (arrow < 0) {
            dx = dir.x - x;
            if (arrow == DIRARROW) {
                dy = dir.y - DataStructure.minsepy - y;
            } else if (arrow == TOARROW) {
                dy = dir.y - y;
            } else {
                // vypindaj
                return;
            }
            final double d = Math.sqrt(dx * dx + dy * dy);
            dx /= d;
            dy /= d;
        } else {
            dx = Math.cos(arrow * Math.PI / 180);
            dy = Math.sin(arrow * Math.PI / 180);
        }
        double x1, y1, x2, y2;
        x1 = x + 1.5 * Node.RADIUS * dx;
        y1 = y + 1.5 * Node.RADIUS * dy;
        if (arrow == TOARROW) {
            x2 = dir.x - 1.5 * Node.RADIUS * dx;
            y2 = dir.y - 1.5 * Node.RADIUS * dy;
        } else {
            x2 = x1 + 2 * Node.RADIUS * dx;
            y2 = y1 + 2 * Node.RADIUS * dy;
        }
        v.setColor(Color.BLACK);
        v.drawArrow((int) x1, (int) y1, (int) x2, (int) y2);
    }

    // Assumption: dir (the node we are pointing to) is above this node
    protected void drawArc(View v) {
        if (!arc || dir == null) {
            return;
        }
        final int x = dir.x, y = this.y - DataStructure.minsepy + Node.RADIUS, a = Math
            .abs(this.x - dir.x), b = Math.abs(this.y - dir.y);
        v.setColor(Color.BLACK);
        if (this.x > dir.x) {
            v.drawArcArrow(x - a, y - b, 2 * a, 2 * b, 0, 90);
        } else {
            v.drawArcArrow(x - a, y - b, 2 * a, 2 * b, 180, 90);
        }
    }

    @Override
    public void draw(View v) {
        if (state == Node.INVISIBLE || getKey() == NULL || state == Node.OUT) {
            return;
        }
        drawBg(v);
        drawKey(v);
        drawArrow(v);
        drawArc(v);
    }

    /**
     * Is the given point inside the node? (Used mainly to decide whether a user
     * clicked at the node.)
     */
    protected boolean inside(int x, int y) {
        return (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) <= Node.RADIUS
            * Node.RADIUS;
    }

    /**
     * Set new coordinates, where the node should go.
     */
    public void goTo(int tox, int toy) {
        this.tox = tox;
        this.toy = toy;
        this.steps = STEPS;
    }

    /**
     * Go to the same position where node v is going.
     */
    public void goTo(Node v) {
        goTo(v.tox, v.toy);
    }

    // public void goAbove (int tox, int toy) { goTo (tox, toy - 2*D.RADIUS -
    // D.yspan); }
    /**
     * Go above node v (or more precisely: above the position where v is going).
     */
    public void goAbove(Node v) {
        goTo(v.tox, v.toy - DataStructure.minsepy);
    }

    // public void goNextTo (int tox, int toy) { goTo (tox + 2*D.RADIUS +
    // D.xspan, toy); }
    /**
     * Go next to node v (precisely to the right of where v is going).
     */
    public void goNextTo(Node v) {
        goTo(v.tox + DataStructure.minsepx, v.toy);
    }

    /**
     * Go to the root position.
     */
    public void goToRoot() {
        goTo(DataStructure.rootx, DataStructure.rooty);
    }

    /**
     * Go above the root position.
     */
    public void goAboveRoot() {
        final int toy = DataStructure.rooty - DataStructure.minsepy;
        goTo(DataStructure.rootx, toy);
    }

    /**
     * Go downwards out of the screen.
     */
    public void goDown() {
        setState(DOWN);
    }

    /**
     * Go left downwards out of the screen.
     */
    public void goLeft() {
        setState(LEFT);
    }

    /**
     * Go right downwards out of the screen.
     */
    public void goRight() {
        setState(RIGHT);
    }

    /**
     * Make one step towards the destination (tox, toy). In the special states
     * DOWN, LEFT, or RIGHT, go downwards off the screen.
     */
    @Override
    public void move() {
        switch (state) {
        case Node.ALIVE:
        case Node.INVISIBLE:
            if (steps > 0) {
                x += (tox - x) / steps;
                y += (toy - y) / steps;
                --steps;
            }
            break;
        case Node.DOWN:
        case Node.LEFT:
        case Node.RIGHT:
            y += 20;
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
        case Node.UP:
            y -= 20;
            break;
        }        
    }

    @Override
    public Rectangle2D getBoundingBox() {
        final int r = RADIUS + 1;
        return new Rectangle2D.Double(x - r, y - r, 2 * r, 2 * r);
    }

    @Override
    public void endAnimation() {
        if (state == ALIVE || state == INVISIBLE) {
            steps = 0;
            x = tox;
            y = toy;
        } else if (state == DOWN || state == LEFT || state == RIGHT) {
            while (D.panel.screen.V.inside(x, y - Node.RADIUS)) {
                y += 20;
                if (state == Node.LEFT) {
                    x -= 20;
                } else if (state == Node.RIGHT) {
                    x += 20;
                }
            }
            state = OUT;
        }
    }

    @Override
    public boolean isAnimationDone() {
        return ((steps == 0 && state == ALIVE) || state == INVISIBLE || state == OUT);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "key", key);
        HashtableStoreSupport.store(state, hash + "state", this.state);
        HashtableStoreSupport.store(state, hash + "tox", tox);
        HashtableStoreSupport.store(state, hash + "toy", toy);
        HashtableStoreSupport.store(state, hash + "color", color);
        HashtableStoreSupport.store(state, hash + "marked", marked);
        HashtableStoreSupport.store(state, hash + "dir", dir);
        HashtableStoreSupport.store(state, hash + "arrow", arrow);
        HashtableStoreSupport.store(state, hash + "arc", arc);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object key = state.get(hash + "key");
        if (key != null) {
            this.key = (Integer) HashtableStoreSupport.restore(key);
        }

        Object stat = state.get(hash + "state");
        if (stat != null) {
            stat = HashtableStoreSupport.restore(stat);
            // tu nechcem mat invisible (inak spravit)
            if ((this.state == OUT || this.state == DOWN || this.state == LEFT
                || this.state == RIGHT || this.state == UP) && stat.equals(ALIVE)) {
                goTo(tox, toy);
            }
            this.state = (Integer) stat;
        }

        boolean isMoved = false;
        Object tox = state.get(hash + "tox");
        Object toy = state.get(hash + "toy");
        if (tox != null) {
            isMoved = true;
        } else {
            tox = this.tox;
        }
        if (toy != null) {
            isMoved = true;
        } else {
            toy = this.toy;
        }
        if (isMoved) {
            goTo((Integer) tox, (Integer) toy);
        }

        final Object color = state.get(hash + "color");
        if (color != null) {
            this.color = (NodeColor) HashtableStoreSupport.restore(color);
        }
        final Object marked = state.get(hash + "marked");
        if (marked != null) {
            this.marked = (Boolean) HashtableStoreSupport.restore(marked);
        }
        final Object dir = state.get(hash + "dir");
        if (dir != null) {
            this.dir = (Node) HashtableStoreSupport.restore(dir);
        }
        final Object arrow = state.get(hash + "arrow");
        if (arrow != null) {
            this.arrow = (Integer) HashtableStoreSupport.restore(arrow);
        }
        final Object arc = state.get(hash + "arc");
        if (arc != null) {
            this.arc = (Boolean) HashtableStoreSupport.restore(arc);
        }
    }
}
