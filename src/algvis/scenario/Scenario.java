package algvis.scenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
	private final String name;
	private final MacroCommand scenario;
	private MacroCommand algorithm = null, step = null;
	private boolean addingEnabled = true;
	private boolean pauses = true;
	private boolean enabled;
	private Thread traverser = null;

	public Scenario(String name) {
		scenario = new MacroCommand(name);
		this.name = name;
		enabled = false;
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setPauses(boolean pauses) {
		this.pauses = pauses;
	}

	public void enableAdding(boolean e) {
		addingEnabled = e;
	}

	public void newAlgorithm() {
		scenario.add(algorithm = new MacroCommand("algorithm"));
	}

	public void newStep() {
		algorithm.add(step = new MacroCommand("step"));
	}

	/**
	 * Appends the command to current step of current algorithm of Scenario.
	 */
	public boolean add(Command c) {
		if (addingEnabled && algorithm != null && step != null) {
			step.add(c);
			return true;
		} else {
			return false;
		}
	}

	public boolean isAlgorithmRunning() {
		return (step.hasNext() || algorithm.hasNext())
				&& (step.hasPrevious() || algorithm.hasPrevious());
	}

	public boolean hasPrevious() {
		return step.hasPrevious() || algorithm.hasPrevious()
				|| scenario.hasPrevious();
	}

	public boolean hasNext() {
		return step.hasNext() || algorithm.hasNext() || scenario.hasNext();
	}

	public void next() {
		enableAdding(false);
		if (pauses) {
			if (step.hasPrevious()) {
				if (algorithm.hasNext()) {
					step = (MacroCommand) algorithm.next();
				} else {
					algorithm = (MacroCommand) scenario.next();
					step = (MacroCommand) algorithm.next();
				}
			} else {
				algorithm.next();
				scenario.next();
			}
			step.execute();
		} else {
			if (step.hasPrevious()) {
				if (!algorithm.hasNext()) {
					algorithm = (MacroCommand) scenario.next();
				}
			} else {
				scenario.next();
			}
			algorithm.execute();
		}
		enableAdding(true);
	}

	public void previous() {
		enableAdding(false);
		if (pauses) {
			step.unexecute();
			algorithm.previous();
			if (algorithm.hasPrevious()) {
				step = (MacroCommand) algorithm.getPrevious();
			} else {
				scenario.previous();
				if (scenario.hasPrevious()) {
					algorithm = (MacroCommand) scenario.getPrevious();
					step = (MacroCommand) algorithm.getPrevious();
				}
			}
		} else {
			algorithm.unexecute();
			scenario.previous();
			if (scenario.hasPrevious()) {
				algorithm = (MacroCommand) scenario.getPrevious();
				step = (MacroCommand) algorithm.getPrevious();
			}
		}
		enableAdding(true);
	}

	public void goToStep(int s) {
		enableAdding(false);
		algorithm.goTo(s);
		if (algorithm.hasPrevious()) {
			step = (MacroCommand) algorithm.getPrevious();
		}
		enableAdding(true);
	}

	public int getAlgPos() {
		return algorithm.getPosIndex();
	}

	/**
	 * returns true if new traverser has been started, else returns false
	 */
	public boolean startNewTraverser(Thread t) {
		if (!killTraverser()) {
			return false;
		} else {
			t.start();
			return true;
		}
	}

	public boolean killTraverser() {
		if (traverser != null) {
			while (traverser.isAlive()) {
				traverser.interrupt();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Element getXML() {
		return scenario.getXML();
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