package algvis.core;

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
	VisPanel M;
	boolean suspended = false;

	public Algorithm(VisPanel M) {
		this.M = M;
	}

	/**
	 * Mysuspend.
	 */
	public void mysuspend() {
		if (M.pause) {
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

	public void setHeader(String s) {
		if (M.pause) {
			M.C.setHeader(s);
		}
	}

	public void setText(String s) {
		if (M.pause) {
			M.C.setText(s);
		}
	}

	public void setText(String s, String... par) {
		if (M.pause) {
			M.C.setText(s, par);
		}
	}

	public void setText(String s, int... par) {
		if (M.pause) {
			M.C.setText(s, par);
		}
	}

	public void run() {
	}
}
