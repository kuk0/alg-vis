package algvis.scenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
	private boolean macroEnabled = false;
	private ArrayList<Command> macro = null;

	public Scenario(String name) {
		scenario = new LinkedList<Command>();
		position = scenario.listIterator();
		this.name = name;
	}

	public void enableAdding(boolean e) {
		addingEnabled = e;
	}

	public void startMacro() {
		endMacro();
		macro = new ArrayList<Command>();
		macroEnabled = true;
	}

	public void endMacro() {
		macroEnabled = false;
		if (macro != null && macro.size() > 0) {
			add(new MacroCommand(macro));
			macro = null;
		}
	}

	/**
	 * Appends the command to current position of Scenario.
	 */
	public boolean add(Command c) {
		if (addingEnabled) {
			if (macroEnabled) {
				macro.add(c);
			} else {
				while (position.hasNext()) {
					position.next();
					position.remove();
				}
				position.add(c);
			}
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
		position.next().execute();
		enableAdding(true);
	}

	public void previous() {
		enableAdding(false);
		position.previous().unexecute();
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