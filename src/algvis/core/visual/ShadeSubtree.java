package algvis.core.visual;

import algvis.core.DataStructure;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.view.Layout;
import algvis.ui.view.View;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.Stack;

public class ShadeSubtree extends VisualElement {
    BSTNode u;
    int x, y;
    Polygon p;

    public ShadeSubtree(BSTNode u) {
        super(Scene.MAXZ - 1);
        this.u = u;
        recompute();
    }

    private void recompute() {
        x = u.x;
        y = u.y;
        p = new Polygon();
        p.addPoint(u.x - 1, u.y - 1);
        if (u.D.getLayout() == Layout.SIMPLE) {
            if (u.height == 1) {
                p.addPoint(u.x - 7, u.y + 10);
                p.addPoint(u.x + 7, u.y + 10);
            } else {
                final int x1 = u.x - u.leftw + DataStructure.minsepx / 2, x2 = u.x
                    + u.rightw - DataStructure.minsepx / 2, y1 = u.y
                    + DataStructure.minsepy, y2 = u.y + (u.height - 1)
                    * DataStructure.minsepy;
                p.addPoint(x1, y1);
                p.addPoint(x1, y2);
                p.addPoint(x2, y2);
                p.addPoint(x2, y1);
            }
        } else {
            // TODO: BST needs to expose threads
            BSTNode w = u;
            final Stack<BSTNode> tmp = new Stack<BSTNode>();
            while (u != null && w != null) {
                p.addPoint(u.x - 1, u.y);
                tmp.add(w);
                u = (u.getLeft() != null) ? u.getLeft() : u.getRight(); // TODO
                w = (w.getRight() != null) ? w.getRight() : w.getLeft();
            }
            while (!tmp.isEmpty()) {
                w = tmp.pop();
                p.addPoint(w.x + 1, w.y);
            }
        }
        p.addPoint(u.x + 1, u.y - 1);
    }

    @Override
    protected void draw(View v) throws ConcurrentModificationException {
        if (u.x != x || u.y != y) {
            recompute();
        }
        v.fillPolygon(p);
    }

    @Override
    protected void move() {
    }

    @Override
    protected Rectangle2D getBoundingBox() {
        return null;
    }

    public int getLeft() {
        return p.xpoints[1];
    }

    public int getRight() {
        return p.xpoints[p.npoints - 2];
    }

    public int getBottom() {
        return p.ypoints[p.npoints / 2];
    }
}