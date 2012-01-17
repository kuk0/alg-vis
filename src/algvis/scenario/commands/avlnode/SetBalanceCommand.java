package algvis.scenario.commands.avlnode;

import org.jdom.Element;

import algvis.avltree.AVLNode;
import algvis.scenario.commands.Command;

public class SetBalanceCommand implements Command {
	private final AVLNode n;
	private final int fromBal, toBal;

	public SetBalanceCommand(AVLNode n, int bal) {
		this.n = n;
		this.fromBal = n.getBalance();
		this.toBal = bal;
	}

	@Override
	public Element getXML() {
		Element e = new Element("setBalance");
		e.setAttribute("key", Integer.toString(n.key));
		e.setAttribute("fromBalance", Integer.toString(fromBal));
		e.setAttribute("toBalance", Integer.toString(toBal));
		return e;
	}

	@Override
	public void execute() {
		n.setBalance(toBal);
	}

	@Override
	public void unexecute() {
		n.setBalance(fromBal);
	}

}
