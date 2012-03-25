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
package algvis.suffixtree;

import algvis.core.Algorithm;

public class SuffixTreeInsert extends Algorithm {
	SuffixTree T;
	String s;

	public SuffixTreeInsert(SuffixTree T, String s) {
		super(T);
		this.T = T;
		this.s = s;
		setHeader("trieinsert", s.substring(0, s.length() - 1));
	}

	public void beforeReturn() {
		T.clearExtraColor();
		addStep("done");
	}

	@Override
	public void run() {
		if (s.compareTo("$") == 0) {
			addNote("badword");
		}
/*
		SuffixTreeNode v = T.getRoot();
		v.mark();
		addNote("trieinsertnote");
		addStep("trierootstart");
		mysuspend();
		v.unmark();
*/
		beforeReturn();
		
	}
}
