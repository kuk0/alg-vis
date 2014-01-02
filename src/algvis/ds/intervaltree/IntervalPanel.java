package algvis.ds.intervaltree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ui.VisPanel;

public class IntervalPanel extends VisPanel {
    private static final long serialVersionUID = -5655533916806349111L;
    public static Class<? extends DataStructure> DS = IntervalTree.class;

    public IntervalPanel(Settings S) {
        super(S);
    }

    @Override
    public void initDS() {
        D = new IntervalTree(this);
        scene.add(D);
        buttons = new IntervalButtons(this);
    }

    @Override
    public void start() {
        super.start();
        D.random(14);
    }
}
