package algvis.core;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JTabbedPane;

public class ADTTabs extends JTabbedPane {
	private static final long serialVersionUID = 2089704386652556353L;
	AlgVis A;
	List<DSTabs> panels;

	public ADTTabs(AlgVis A) {
		super();
		this.A = A;
		panels = new LinkedList<DSTabs>();
	}

	void addTab(DSTabs tabs) {
		super.addTab(A.getString(tabs.getTitle()), tabs);
		panels.add(tabs);
	}

	void refresh() {
		int i = 0;
		for (DSTabs t : panels) {
			setTitleAt(i, A.getString(t.getTitle()));
			t.refresh();
			++i;
		}
	}
}
