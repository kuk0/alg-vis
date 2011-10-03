package algvis;

public class Algorithm extends Thread {
	VisPanel M;
	boolean suspended = false;

	public Algorithm(VisPanel M) {
		this.M = M;
	}

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
