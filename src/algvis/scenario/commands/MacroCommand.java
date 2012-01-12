package algvis.scenario.commands;

import java.util.ArrayList;
import java.util.ListIterator;

import org.jdom.Element;

public class MacroCommand implements Command {
	private final ArrayList<Command> commands;

	@SuppressWarnings("unchecked")
	public MacroCommand(ArrayList<Command> commands) {
		this.commands = (ArrayList<Command>) commands.clone();
	}

	@Override
	public void execute() {
		for (Command c : commands) {
			c.execute();
		}
	}

	@Override
	public void unexecute() {
		for (ListIterator<Command> i = commands.listIterator(commands.size()); i
				.hasPrevious();) {
			i.previous().unexecute();
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element("step");
		for (Command c : commands) {
			e.addContent(c.getXML());
		}
		return e;
	}

}
