package algvis.core;

import java.awt.Graphics;
import java.util.Random;


abstract public class DataStructure {
	// datova struktura musi vediet gombikom povedat, kolko ich potrebuje,
	// kolko ma vstupov, ake to su a co treba robit
	Algorithm A;
	public VisPanel M;
	public int radius = 10, xspan = 15, yspan = 5, rootx = 0, rooty = 0,
			sheight = 600, swidth = 400;
	public int x1, x2, y1 = -50, y2;
	public Node chosen = null;

	public DataStructure(VisPanel M) {
		this.M = M;
	}

	public void next() {
		A.myresume();
	}

	abstract public String stats();

	abstract public void insert(int x);

	abstract public void clear();

	abstract public void clean();

	abstract public void draw(Graphics g, View v);

	protected void start(Algorithm a) {
		unmark();
		A = a;
		M.B.enableNext();
		A.start();
		try {
			A.join();
			// System.out.println("join");
		} catch (InterruptedException e) {
			System.out.println("AJAJAJ");
		}
		setStats();
		M.B.disableNext();
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
	
	public void mouseClicked(int x, int y) {}
	
	public void unmark() {
		if (chosen != null) {
			chosen.unmark();
			chosen = null;
		}
	}
}
