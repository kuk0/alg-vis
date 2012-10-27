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
package algvis.ds.suffixtree;

import algvis.core.Algorithm;

public class SuffixTreeFind extends Algorithm {
	private final SuffixTree T;
	private final String s;

	public SuffixTreeFind(SuffixTree T, String s) {
		super(T.panel);
		this.T = T;
		this.s = s;
	}

	void beforeReturn() {
		T.clearExtraColor();
		addStep("done");
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("triefind", s.substring(0, s.length() - 1));
		if (s.compareTo("$") == 0) {
			addNote("badword");
		}
		/*
		  SuffixTreeNode v = T.getRoot();
		  addNote("triefindnote");
		  addStep("trierootstart");
		  v.mark();
		  pause();
		  v.unmark();
		  T.hw = new SuffixTreeWordNode(T, s);
		  T.hw.setC(NodeColor.CACHED);
		  T.hw.goNextTo(v);
  */
		beforeReturn();
	}
}
