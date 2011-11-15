package algvis.scenario;

import java.util.Vector;

import org.jdom.Element;

public class SubScenario implements Scenariable<Command> {
	protected Vector<Command> commands;
	protected int position;
	protected boolean canAdd;
	protected String name = "subScenario";

	public SubScenario() {
		commands = new Vector<Command>();
		position = -1;
		canAdd = true;
	}

	public SubScenario(String name) {
		this();
		this.name = name;
	}

	@Override
	public void add(Command c) {
		if (canAdd) {
			commands.add(c);
			++position;
		}
	}

	@Override
	public boolean previous() {
		if (position == -1)
			return false;

		canAdd = false;
		commands.elementAt(position).unexecute();
		while (--position > 0
				&& !(commands.elementAt(position) instanceof PauseCommand)) {
			commands.elementAt(position).unexecute();
		}
		canAdd = true;

		return true;
	}

	@Override
	public boolean next() {
		if (position >= length() - 1)
			return false;

		canAdd = false;
		++position;
		do {
			commands.elementAt(position).execute();
			++position;
		} while (position < length()
				&& !(commands.elementAt(position) instanceof PauseCommand));
		if (position == length())
			--position;
		canAdd = true;

		return true;
	}

	@Override
	public int length() {
		return commands.size();
	}

	@Override
	public Element getXML() {
		Element e = new Element(this.name());
		for (int i = 0; i < length(); ++i) {
			e.addContent(commands.elementAt(i).getXML());
		}
		return e;
	}

	@Override
	public String name() {
		return name;
	}

}
