package algvis.core;

import java.util.Random;

import algvis.bst.BST;
import algvis.scenario.Scenario;

abstract public class DataStructure {
	// datova struktura musi vediet gombikom povedat, kolko ich potrebuje,
	// kolko ma vstupov, ake to su a co treba robit
	Algorithm A;
	public VisPanel M;
	public Scenario scenario;
	public static int rootx = 0, rooty = 0, sheight = 600, swidth = 400,
			minsepx = 38, minsepy = 30;
	public int x1, x2, y1 = -50, y2;
	public Node chosen = null;
	public static String adtName = "";
	public static String dsName = "";

	public DataStructure(VisPanel M) {
		this.M = M;
		scenario = new Scenario(M, getName());
	}

	abstract public String getName();

	abstract public String stats();

	abstract public void insert(int x);

	abstract public void clear();

	abstract public void draw(View v);

	public void next() {
		A.myresume();
	}

	protected void start(Algorithm a) {
		unmark();
		A = a;
		M.B.enableNext();
		M.B.enablePrevious();
		M.B.disableAll();

		try {
			A.start();
		} catch (IllegalThreadStateException e) {
			System.out.println("LOL");
			M.B.disableNext();
			M.B.enableAll();
			return;
		}
		try {
			A.join();
			// System.out.println("join");
		} catch (InterruptedException e) {
			System.out.println("AJAJAJ");
		}
		setStats();
		M.B.disableNext();
		M.B.enableAll();
	}

	protected void setStats() {
		M.B.setStats(stats());
	}

	/*
	 * protected void showStatus(String t) { M.showStatus(t); }
	 */

	public double lg(int x) { // log_2
		return Math.log(x) / Math.log(2);
	}

	public void random(final int n) {
		// TODO startNew should go upper (to Buttons)
		scenario.traverser.startNew(new Runnable() {
			@Override
			public void run() {
				Random g = new Random(System.currentTimeMillis());
				boolean p = M.pause;
				M.pause = false;
				for (int i = 0; i < n; ++i) {
					if (M.D instanceof BST) {
						scenario.newAlgorithm();
						scenario.newStep();
					}
					insert(g.nextInt(InputField.MAX + 1));
					setStats();
				}
				M.pause = p;
				M.C.update();
				M.B.update();
			}
		}, false);
	}

	public void unmark() {
		if (chosen != null) {
			chosen.unmark();
			chosen = null;
		}
	}

	public Layout getLayout() {
		return M.S.layout;
	}
}
