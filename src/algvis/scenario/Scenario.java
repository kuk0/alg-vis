package algvis.scenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import algvis.core.VisPanel;
import algvis.scenario.commands.Command;
import algvis.scenario.commands.MacroCommand;
import algvis.scenario.commands.ScenarioCommand;

/**
 * Scenario (or history list) stores list of Commands, which are executed. It
 * enables the world to traverse through the list and save it as XML file.
 */
public class Scenario implements XMLable {
	private final ScenarioCommand scenario;
	public final Traverser traverser;
	private VisPanel V;
	private boolean addingEnabled = true;
	private boolean enabled;

	public Scenario(VisPanel V, String name) {
		this.V = V;
		scenario = new ScenarioCommand(name);
		traverser = new Traverser();
		enabled = false;
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void enableAdding(boolean e) {
		addingEnabled = e;
	}

	public boolean isAddingEnabled() {
		return addingEnabled;
	}

	public void newAlgorithm() {
		scenario.add(new MacroCommand<MacroCommand<Command>>("algorithm"));
	}

	public void newStep() {
		scenario.getCurrent().add(new MacroCommand<Command>("step"));
	}

	/**
	 * Appends the command to current step of current algorithm of Scenario.
	 */
	public void add(Command c) {
		if (addingEnabled && !scenario.isEmpty()
				&& !scenario.getCurrent().isEmpty()) {
			scenario.getCurrent().getCurrent().add(c);
		}
	}

	public boolean isAlgorithmRunning() {
		if (scenario.isEmpty()) {
			return false;
		} else {
			return scenario.getCurrent().hasPrevious()
					&& scenario.getCurrent().hasNext();
		}
	}

	public boolean hasPrevious() {
		return scenario.hasPrevious();
	}

	public boolean hasNext() {
		return scenario.hasNext();
	}

	public void next(final boolean pauses, boolean visible) {
		traverser.startNew(new Runnable() {
			@Override
			public void run() {
				enableAdding(false);
				if (pauses) {
					scenario.executeOne();
				} else {
					scenario.execute();
				}
				V.B.update();
				enableAdding(true);
			}
		}, visible);
	}

	public void previous(final boolean pauses, boolean visible) {
		traverser.startNew(new Runnable() {
			@Override
			public void run() {
				enableAdding(false);
				if (pauses) {
					scenario.unexecuteOne();
				} else {
					scenario.unexecute();
				}
				V.B.update();
				enableAdding(true);
			}
		}, visible);
	}

	/*
	 * go at position in algorithm before beginning of s-th step
	 */
	public void goBeforeStep(final int s, boolean visible) {
		traverser.startNew(new Runnable() {
			@Override
			public void run() {
				enableAdding(false);
				try {
					scenario.getCurrent().goBefore(s);
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				}
				V.B.update();
				enableAdding(true);
			}
		}, visible);
	}

	public int getAlgPos() {
		return scenario.getCurrent().getPosition();
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
			e.printStackTrace();
		}
	}

	public class Traverser {
		private Thread threadInstance = new Thread();
		private boolean interrupted = false;

		public void startNew(Runnable r, boolean visible) {
			startNew(new Thread(r), visible);
		}

		public void startNew(Thread t, boolean visible) {
			try {
				setThreadInstance(t);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			threadInstance.start();
			if (!visible) {
				threadInstance.interrupt();
				interrupted = true;
			} else {
				interrupted = false;
			}
		}

		private void setThreadInstance(Thread t) throws Exception {
			interrupted = true;
			threadInstance.interrupt();
			int c = 0;
			while (threadInstance.isAlive()) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				++c;
				// old thread is alive for more than 2 seconds
				if (c > 40) {
					throw new Exception(
							"Error: Traverser - cannot kill current thread within 2 seconds.");
				}
			}
			threadInstance = t;
			interrupted = false;
		}

		public boolean isInterrupted() {
			return interrupted;
		}

		public void join() {
			try {
				threadInstance.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}