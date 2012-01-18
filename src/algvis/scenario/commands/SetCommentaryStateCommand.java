package algvis.scenario.commands;

import org.jdom.Element;

import algvis.core.Commentary;
import algvis.core.Commentary.State;

public class SetCommentaryStateCommand implements Command {
	private final State fromState, toState;
	private final Commentary c;

	public SetCommentaryStateCommand(Commentary c, State fromState) {
		this.c = c;
		this.fromState = fromState;
		this.toState = c.getState();
	}

	@Override
	public Element getXML() {
		Element e = new Element("saveCommentary");
		return e;
	}

	@Override
	public void execute() {
		c.restoreState(toState);
	}

	@Override
	public void unexecute() {
		c.restoreState(fromState);
	}

}
