package algvis.ds.dictionaries.btree;

import algvis.ui.VisPanel;

public class BPlusPanel extends VisPanel {
    private static final long serialVersionUID = 8114215268825709283L;

    @Override
    public void initDS() {
        D = new BPlusTree(this);
        scene.add(D);
        buttons = new BPlusTreeButtons(this);
    }

    @Override
    public void start() {
        super.start();
        D.random(25);
    }
}
