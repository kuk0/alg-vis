package algvis;

import java.awt.Color;

public class GBNode extends BSTNode {
	boolean deleted = false;
	static final Color DELETED = Color.DARK_GRAY;

	public GBNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public GBNode(DataStructure D, int key) {
		super(D, key);
	}
}
