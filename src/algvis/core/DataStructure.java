package algvis.core;

import java.util.Random;

import algvis.scenario.Scenario;

abstract public class DataStructure {
	// datova struktura musi vediet gombikom povedat, kolko ich potrebuje,
	// kolko ma vstupov, ake to su a co treba robit
	Algorithm A;
	public VisPanel M;
	public Scenario scenario;
	public int radius = 10, xspan = 15, yspan = 5, rootx = 0, rooty = 0,
			sheight = 600, swidth = 400, minsepx = 38, minsepy = 30;
	public int x1, x2, y1 = -50, y2;
	public Node chosen = null;
	public static String adtName = ""; // unused field?
	public static String dsName = ""; // also here? Subclasses of this also have static dsName...

	public DataStructure(VisPanel M, String dsName) {
		this.M = M;
		scenario = new Scenario(dsName);
	}

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
		A.start();
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

	/*protected void showStatus(String t) {
		M.showStatus(t);
	}*/

	public double lg(int x) { // log_2
		return Math.log(x) / Math.log(2);
	}

	public void random(int n) {
		Random g = new Random(System.currentTimeMillis());
		boolean p = M.pause;
		M.pause = false;
		for (int i = 0; i < n; ++i) {
			insert(g.nextInt(InputField.MAX + 1));
		}
		M.pause = p;
	}

	public void unmark() {
		if (chosen != null) {
			chosen.unmark();
			chosen = null;
		}
	}
}
