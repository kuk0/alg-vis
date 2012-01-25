package algvis.scenario.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jdom.Element;

public class MacroCommand implements Command {
	private final List<Command> commands;
	private final ListIterator<Command> position;
	private final String name;
	private int positionIndex;

	public MacroCommand(String name) {
		commands = new ArrayList<Command>();
		position = commands.listIterator();
		this.name = name;
		positionIndex = -1;
	}

	public void add(Command c) {
		// remove all subsequent commands
		while (position.hasNext()) {
			position.next();
			position.remove();
		}
		position.add(c);
		positionIndex = commands.size() - 1;
	}

	public Command next() {
		positionIndex = position.nextIndex();
		return position.next();
	}

	public Command previous() {
		positionIndex = position.previousIndex();
		return position.previous();
	}

	public boolean hasNext() {
		return position.hasNext();
	}

	public boolean hasPrevious() {
		return position.hasPrevious();
	}

	/**
	 * This method does not change position! Use previous() to traverse macro!
	 */
	public Command getPrevious() {
		Command result = null;
		if (position.hasPrevious()) {
			result = position.previous();
			position.next();
		}
		return result;
	}

	/**
	 * This method does not change position! Use next() to traverse macro!
	 */
	public Command getNext() {
		Command result = null;
		if (position.hasNext()) {
			result = position.next();
			position.previous();
		}
		return result;
	}

	public int getPosIndex() {
		return positionIndex;
	}

	/** go at position after executing step s */
	public void goTo(int s) {
		ListIterator<Command> it = commands.listIterator(s);
		while (position.previousIndex() > it.nextIndex()) {
			position.previous().unexecute();
		}
		while (position.nextIndex() <= it.nextIndex()) {
			position.next().execute();
		}
	}

	@Override
	public void execute() {
		while (position.hasNext()) {
			position.next().execute();
		}
	}

	@Override
	public void unexecute() {
		while (position.hasPrevious()) {
			position.previous().unexecute();
		}
	}

	@Override
	public Element getXML() {
		Element e = new Element(name);
		int k = 1;
		for (Command c : commands) {
			Element ch = c.getXML();
			ch.setAttribute("order", Integer.toString(k++));
			e.addContent(ch);
		}
		return e;
	}

}
