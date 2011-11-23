package algvis.scenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Scenario (or history list) stores list of Commands, which are executed. It
 * enables the world to traverse through the list and save it as XML file.
 */
public class Scenario extends LinkedList<Command> implements XMLable {
	/**
	 * Eclipse has told me I should add this line, because Scenario is
	 * serializable. hmm... ???
	 */
	private static final long serialVersionUID = -2527919523280478542L;
	private String name;
	private ListIterator<Command> position;
	private boolean addingEnabled;

	public Scenario(String name) {
		super();
		position = listIterator();
		this.name = name;
		enableAdding(true);
	}

	public void enableAdding(boolean e) {
		addingEnabled = e;
	}

	/**
	 * Appends the command to current position of Scenario.
	 */
	@Override
	public boolean add(Command c) {
		if (addingEnabled) {
			position.add(c);
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
		Command current = position.next();
		current.execute();
		while (hasNext()) {
			current = position.next();
			current.execute();
			if (current instanceof PauseCommand) {
				break;
			}
		}
		enableAdding(true);
	}

	public void previous() {
		enableAdding(false);
		Command current = position.previous();
		current.unexecute();
		while (hasPrevious()) {
			current = position.previous();
			current.unexecute();
			if (current instanceof PauseCommand) {
				break;
			}
		}
		enableAdding(true);
	}

	@Override
	public Element getXML() {
		Element root = new Element(name);
		Element algorithm = null;
		Element step = null;
		for (Command c : this) {
			if (c instanceof NewAlgorithmCommand) {
				if (algorithm != null) {
					root.addContent(algorithm);
				}
				algorithm = new Element(((NewAlgorithmCommand) c).getName());
				step = new Element("step");
			} else if (c instanceof PauseCommand) {
				if (step != null) {
					algorithm.addContent(step);
				}
				step = new Element("step");
			} else {
				if (step == null) {
					System.out.println("Something went bad :(");
					System.out
							.println("Bad scenario - some commands are missing.");
				} else
					step.addContent(c.getXML());
			}
		}
		if (algorithm != null) {
			root.addContent(algorithm);
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