/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.scenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import algvis.gui.VisPanel;

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

	/** for many reasons must be at least 1 */
	public static final int maxAlgorithms = 47;

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
		if (addingEnabled) {
			scenario.add(new MacroCommand<MacroCommand<Command>>("algorithm"));
		}
	}

	public void newStep() {
		if (addingEnabled) {
			scenario.getCurrent().add(new MacroCommand<Command>("step"));
		}
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

	public void clear() {
		scenario.clear();
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
				V.C.update();
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
				V.C.update();
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
		if (scenario.isEmpty()) {
			return -1;
		} else {
			return scenario.getCurrent().getPosition();
		}
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
			if (!visible) {
				interrupted = true;
			} else {
				interrupted = false;
			}
			threadInstance.start();
			if (!visible) {
				threadInstance.interrupt();
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

	private class ScenarioCommand extends
			MacroCommand<MacroCommand<MacroCommand<Command>>> {

		public ScenarioCommand(String name) {
			super(name);
		}

		public void clear() {
			commands.clear();
			iterator = commands.listIterator();
			current = null;
			position = -1;
		}

		@Override
		public void add(MacroCommand<MacroCommand<Command>> c) {
			super.add(c);
			if (position == maxAlgorithms) {
				commands.remove(0);
				iterator = commands.listIterator(commands.size());
				current = iterator.previous();
				iterator.next();
				position = iterator.previousIndex();
			}
		}

		@Override
		public void unexecuteOne() {
			if (current.hasPrevious()) {
				current.unexecuteOne();
				if (!current.hasPrevious()
						&& iterator.previousIndex() == position) {
					iterator.previous();
				}
			} else {
				position = iterator.previousIndex();
				current = iterator.previous();
				current.unexecuteOne();
			}
			if (!current.hasPrevious() && iterator.hasPrevious()) {
				position = iterator.previousIndex();
				current = iterator.previous();
				iterator.next();
			}
		}

		@Override
		public void executeOne() {
			if (current.hasNext()) {
				current.executeOne();
				if (!current.hasNext() && iterator.nextIndex() == position) {
					iterator.next();
				}
			} else {
				position = iterator.nextIndex();
				current = iterator.next();
				current.executeOne();
			}
		}

		@Override
		public void execute() {
			if (current.hasNext()) {
				current.execute();
				if (!current.hasNext() && iterator.nextIndex() == position) {
					iterator.next();
				}
			} else {
				position = iterator.nextIndex();
				current = iterator.next();
				current.execute();
			}
		}

		@Override
		public void unexecute() {
			if (current.hasPrevious()) {
				current.unexecute();
				if (!current.hasPrevious()
						&& iterator.previousIndex() == position) {
					iterator.previous();
				}
			} else {
				position = iterator.previousIndex();
				current = iterator.previous();
				current.unexecute();
			}
			if (!current.hasPrevious() && iterator.hasPrevious()) {
				position = iterator.previousIndex();
				current = iterator.previous();
				iterator.next();
			}
		}
	}
}
