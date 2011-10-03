package algvis.core;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JTabbedPane;

public class DSTabs extends JTabbedPane {
	private static final long serialVersionUID = -1069773411088675102L;
	AlgVis A;
	List<VisPanel> panels;
	String title;

	public DSTabs(AlgVis A, String title) {
		super();
		this.A = A;
		panels = new LinkedList<VisPanel>();
		this.title = title;
	}

	void addTab(VisPanel p) {
		super.addTab(A.getString(p.getTitle()), p);
		panels.add(p);
	}

	String getTitle() {
		return title;
	}

	void refresh() {
		int i = 0;
		for (VisPanel p : panels) {
			setTitleAt(i, A.getString(p.getTitle()));
			p.refresh();
			++i;
		}
	}
}
