package algvis.core;

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

	public Algorithm(DataStructure D) {
		this.D = D;
		D.scenario.newAlgorithm();
		D.scenario.newStep();
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
		D.scenario.newStep();
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

	public void setHeader(String s) {
		D.M.C.setHeader(s);
	}

	public void setHeader(String s, String... par) {
		D.M.C.setHeader(s, par);
	}

	public void setHeader(String s, int... par) {
		D.M.C.setHeader(s, par);
	}

	public void addNote(String s) {
		D.M.C.addNote(s);
	}

	public void addNote(String s, String... par) {
		D.M.C.addNote(s, par);
	}

	public void addNote(String s, int... par) {
		D.M.C.addNote(s, par);
	}

	public void addStep(String s) {
		D.M.C.addStep(s);
	}

	public void addStep(String s, String... par) {
		D.M.C.addStep(s, par);
	}

	public void addStep(String s, int... par) {
		D.M.C.addStep(s, par);
	}

	@Override
	public void run() {
	}
}
