package algvis.core;

import algvis.scenario.commands.HardPauseCommand;
import algvis.scenario.commands.SetCommentaryStateCommand;

/**
 * The Class Algorithm.
 * Each visualized data structure consists of data and algorithms (such as insert, delete)
 * that update the data. All such algorithms are descendants of the class Algorithm.
 * 
 * A visualized algorithm has its own thread which can be suspended (e.g., after each step
 * of the algorithm; see method mysuspend) and is automatically resumed (method myresume)
 * after pressing the "Next" button.   
 */
abstract public class Algorithm extends Thread {
	private DataStructure D;
	boolean suspended = false;
	private algvis.core.Commentary.State commentaryState;

	/**
	 * rather use Algorithm(DataStructure D, String name)
	 */
	public Algorithm(DataStructure D) {
		this.D = D;
		D.scenario.startMacro();
		D.scenario.add(new HardPauseCommand(D, false));
		commentaryState = D.M.C.getState();
	}

	/**
	 * Mysuspend.
	 */
	public void mysuspend() {
		D.scenario.startMacro();
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
		D.scenario.endMacro();
	}
	
	public void setHeader(String s) {
		if (D.M.pause) {
			D.M.C.setHeader(s);
			saveCommentary();
		}
	}

	public void setText(String s) {
		if (D.M.pause) {
			D.M.C.setText(s);
			saveCommentary();
		}
	}

	public void setText(String s, String... par) {
		if (D.M.pause) {
			D.M.C.setText(s, par);
			saveCommentary();
		}
	}

	public void setText(String s, int... par) {
		if (D.M.pause) {
			D.M.C.setText(s, par);
			saveCommentary();
		}
	}

	private void saveCommentary() {
		D.scenario.add(new SetCommentaryStateCommand(D.M.C, commentaryState));
		commentaryState = D.M.C.getState();
	}

	public void run() {
	}
}
