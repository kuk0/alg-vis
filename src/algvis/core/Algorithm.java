package algvis.core;

import algvis.scenario.commands.HardPauseCommand;
import algvis.scenario.commands.SetCommentaryStateCommand;

/**
 * The Class Algorithm. Each visualized data structure consists of data and
 * algorithms (such as insert, delete) that update the data. All such algorithms
 * are descendants of the class Algorithm.
 * 
 * A visualized algorithm has its own thread which can be suspended (e.g., after
 * each step of the algorithm; see method mysuspend) and is automatically
 * resumed (method myresume) after pressing the "Next" button.
 */
abstract public class Algorithm extends Thread {
	private DataStructure D;
	boolean suspended = false;
	private algvis.core.Commentary.State commentaryState;

	public Algorithm(DataStructure D) {
		this.D = D;
		D.scenario.addingNextStep();
		D.scenario.add(new HardPauseCommand(D, false));
		commentaryState = D.M.C.getState();
	}

	/**
	 * Mysuspend.
	 */
	public void mysuspend() {
		if (D.M.pause) {
			suspended = true;
			synchronized (this) {
				try {
					while (suspended) {
						wait();
					}
				} catch (InterruptedException e) {
				}
			}
		}
		D.scenario.addingNextStep();
	}

	/**
	 * Myresume.
	 */
	public void myresume() {
		// if (suspended)
		synchronized (this) {
			suspended = false;
			notifyAll();
		}
	}

	protected void finish() {
		D.scenario.add(new HardPauseCommand(D, true));
	}

	public void setHeader(String s) {
		D.M.C.setHeader(s);
		saveCommentary();
	}

	public void addNote(String s) {
		D.M.C.addNote(s);
		saveCommentary();
	}

	public void addStep(String s) {
		D.M.C.addStep(s);
		saveCommentary();
	}

	public void addStep(String s, String... par) {
		D.M.C.addStep(s, par);
		saveCommentary();
	}

	public void addStep(String s, int... par) {
		D.M.C.addStep(s, par);
		saveCommentary();
	}

	private void saveCommentary() {
		D.scenario.add(new SetCommentaryStateCommand(D.M.C, commentaryState));
		commentaryState = D.M.C.getState();
	}

	@Override
	public void run() {
	}
}
