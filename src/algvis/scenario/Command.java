package algvis.scenario;

import org.jdom.Element;

import algvis.core.Node;

public abstract class Command {
	protected Node n;
	
	public Command(Node n) {
		this.n = n;
	}
	
	public abstract void execute();
	public abstract void unexecute();
	public abstract Element getXML();
}
