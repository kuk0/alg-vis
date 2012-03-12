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
package algvis.core;

import java.util.Random;

import algvis.scenario.Scenario;

abstract public class DataStructure {
	// datova struktura musi vediet gombikom povedat, kolko ich potrebuje,
	// kolko ma vstupov, ake to su a co treba robit
	protected Algorithm A;
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

	public void random(int n) {
		Random g = new Random(System.currentTimeMillis());
		boolean p = M.pause;
		M.pause = false;
		{
			int i = 0;
			scenario.enableAdding(false);
			M.C.enableUpdating(false);
			for (; i < n - Scenario.maxAlgorithms; ++i) {
				insert(g.nextInt(InputField.MAX + 1));
			}
			scenario.enableAdding(true);
			for (; i < n; ++i) {
				insert(g.nextInt(InputField.MAX + 1));
			}
			M.C.enableUpdating(true);
			M.C.update();
		}
		M.pause = p;
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
