package algvis.core;

import algvis.scenario.AlgorithmScenario;
import algvis.scenario.PauseCommand;

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

	// only for compatibility until scenario is added to all algorithms
	public Algorithm(DataStructure D) {
		this.D = D;
	}
	
	public Algorithm(DataStructure D, String name) {
		this(D);
		D.subScenario = new AlgorithmScenario(name);
		D.scenario.add(D.subScenario);
	}

	/**
	 * Mysuspend.
	 */
	public void mysuspend() {
		if (D.subScenario != null) {
			D.subScenario.add(new PauseCommand());
		}
		if (D.M.pause) {
			if (D.subScenario != null) {
				D.subScenario.canAdd = false;
			}
			suspended = true;
			synchronized (this) {
				try {
					while (suspended) {
						wait();
					}
				} catch (InterruptedException e) {
				} finally {
					if (D.subScenario != null) {
						D.subScenario.canAdd = true;
					}
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
		if (D.subScenario != null) {
			D.subScenario.canAdd = false;
			D.subScenario = null;
		}
	}
	
	public void setHeader(String s) {
		if (D.M.pause) {
			D.M.C.setHeader(s);
		}
	}

	public void setText(String s) {
		if (D.M.pause) {
			D.M.C.setText(s);
		}
	}

	public void setText(String s, String... par) {
		if (D.M.pause) {
			D.M.C.setText(s, par);
		}
	}

	public void setText(String s, int... par) {
		if (D.M.pause) {
			D.M.C.setText(s, par);
		}
	}

	public void run() {
	}
}
