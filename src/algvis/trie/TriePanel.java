package algvis.trie;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class TriePanel extends VisPanel {
	private static final long serialVersionUID = -8652425842838569507L;
	public static Class<? extends DataStructure> DS = Trie.class;

	public TriePanel(Settings S) {
		super(S);
	}
	
	@Override
	public void initDS() {
		D = new Trie(this);
		B = new TrieButtons(this);
	}

}
