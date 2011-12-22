package algvis.scenario;

import org.jdom.Element;

import algvis.avltree.AVLNode;

public class SetBalanceCommand implements Command {
	private AVLNode n;
	private int fromBal, toBal;

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
