 package algvis.linkcuttree;
 
import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.TreeNode;
import algvis.gui.view.View;

public class LinkCutDSNode extends TreeNode {
	LinkCutDSNode preffered = null;

	public LinkCutDSNode(DataStructure D, int key) {
		super(D, key);
	}
	
	public LinkCutDSNode getParent() {
		return (LinkCutDSNode) super.getParent();
	} 
	
	public LinkCutDSNode getNode(int x) {
		if (getKey() == x) {return this;}
		LinkCutDSNode w = (LinkCutDSNode) getChild();
		LinkCutDSNode res = null;
		while ((w != null) && (res == null)) {
			res = w.getNode(x);
			w = (LinkCutDSNode) w.getRight();
		}
		return res;
	}
	
	@Override
	public void drawEdges(View v) {
		if (state != INVISIBLE) {
			if (thread) {
				v.setColor(Color.red); // TODO
				if (getChild() != null) {
					if (preffered == getChild()) {
						v.drawLine(x, y, getChild().x, getChild().y);
					} else {
						v.drawDashedLine(x, y, getChild().x, getChild().y);							
					}
				}
				v.setColor(Color.black);
			} else {
				TreeNode w = getChild();
				while (w != null) {
					v.setColor(Color.black); // TODO maybe these lines would
												// make problems
					if (w == preffered) {
						v.drawLine(x, y, w.x, w.y);
					} else {
						v.drawDashedLine(x, y, w.x, w.y);
					}
					w.drawEdges(v);
					w = w.getRight();
				}
			}
		}
	}

}
