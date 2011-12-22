package algvis.scenario.commands.bstnode;

import org.jdom.Element;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.scenario.commands.Command;

public class SetBSTRootCommand implements Command {
	private BST T;
	private BSTNode root, previousRoot;

	public SetBSTRootCommand(BST T, BSTNode root, BSTNode previousRoot) {
		this.T = T;
		this.root = root;
		this.previousRoot = previousRoot;
	}

	@Override
	public void execute() {
		T.setRoot(root);
	}

	@Override
	public void unexecute() {
		T.setRoot(previousRoot);
	}

	@Override
	public Element getXML() {
		Element e = new Element("setRoot");
		if (root != null) {
			e.setAttribute("rootKey", Integer.toString(root.key));
		} else {
			e.setAttribute("root", "null");
		}
		if (previousRoot != null) {
			e.setAttribute("previousRootKey",
					Integer.toString(previousRoot.key));
		} else {
			e.setAttribute("previousRoot", "null");
		}
		return e;
	}

}
