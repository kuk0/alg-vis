package algvis.scenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import algvis.scenario.commands.Command;
import algvis.scenario.commands.MacroCommand;

/**
 * Scenario (or history list) stores list of Commands, which are executed. It
 * enables the world to traverse through the list and save it as XML file.
 */
public class Scenario implements XMLable {
	private String name;
	private List<Command> scenario;
	private ListIterator<Command> position;
	private boolean addingEnabled = true;
	private boolean pauses = true, stopped = false;
	private MacroCommand macro = null;

	public Scenario(String name) {
		scenario = new LinkedList<Command>();
		position = scenario.listIterator();
		this.name = name;
		macro = new MacroCommand();
		/* TODO delete this line */
	}

	public void stop() {
		stopped = true;
	}

	public void setPauses(boolean pauses) {
		this.pauses = pauses;
	}

	public void enableAdding(boolean e) {
		addingEnabled = e;
	}

	public void addingNextStep() {
		macro = new MacroCommand();
		while (position.hasNext()) {
			position.next();
			position.remove();
		}
		position.add(macro);
	}

	/**
	 * Appends the command to current position of Scenario.
	 */
	public boolean add(Command c) {
		if (addingEnabled) {
			macro.add(c);
			return true;
		} else {
			return false;
		}
	}

	public boolean hasPrevious() {
		return position.hasPrevious();
	}

	public boolean hasNext() {
		return position.hasNext();
	}

	public void next() {
		enableAdding(false);
		if (pauses) {
			position.next().execute();
		} else {
			while (hasNext() && !stopped) {
				position.next().execute();
			}
			stopped = false;
		}
		enableAdding(true);
	}

	public void previous() {
		enableAdding(false);
		if (pauses) {
			position.previous().unexecute();
		} else {
			while (hasPrevious() && !stopped) {
				position.previous().unexecute();
			}
			stopped = false;
		}
		enableAdding(true);
	}

	@Override
	public Element getXML() {
		Element root = new Element(name);
		for (Command c : scenario) {
			root.addContent(c.getXML());
		}
		return root;
	}

	public void saveXML(String path) {
		Document doc = new Document(getXML());
		XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(path));
			outp.output(doc, outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}