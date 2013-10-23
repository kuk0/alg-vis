package algvis.core.visual;

import algvis.core.StringUtils;
import algvis.core.history.HashtableStoreSupport;
import algvis.internationalization.Stringable;
import algvis.ui.view.REL;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

public class TextBubble extends VisualElement {
    Stringable s;
    int x, y, w, alpha, toalpha;
    REL pos;
    public static final int DA = 21;

    public TextBubble(Stringable s, int x, int y, int w, REL pos) {
        super(0);
        this.s = s;
        this.x = x;
        this.y = y;
        this.w = w;
        this.alpha = 0;
        this.toalpha = (256 / DA) * DA;
        ;
        this.pos = pos;
    }

    @Override
    public void draw(View V) {
        V.drawTextBubble(StringUtils.unHtml(s.getString()), x, y, w, alpha, pos);
    } // TODO: unHtml only until we get rid of the commentary

    @Override
    public void move() {
        if (alpha < toalpha) {
            alpha += DA;
        } else if (alpha > toalpha) {
            alpha -= DA;
        }
    }

    @Override
    protected void endAnimation() {
        toalpha = 0;
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return null;
    }

    @Override
    protected boolean isAnimationDone() {
        return toalpha == 0 && alpha == 0;  //state == 1 && alpha <= 0;
    }

    boolean init_hack = true;

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        if (init_hack) {
            HashtableStoreSupport.store(state, hash + "toalpha", 0);
            init_hack = false;
        } else {
            HashtableStoreSupport.store(state, hash + "toalpha", toalpha);
        }
        //System.out.println("hou: " + s.getString() + toalpha);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object toa = state.get(hash + "toalpha");
        if (toa != null) {
            this.toalpha = (Integer) HashtableStoreSupport.restore(toa);
        }
    }
}