/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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

public class MyRandom {
	static Random G = new Random(System.currentTimeMillis());

	public static boolean heads() {
		return G.nextInt(2) == 1;
	}

	public static boolean tails() {
		return G.nextInt(2) == 0;
	}

	public static int bit() {
		return G.nextInt(2);
	}

	public static int Int(int n) {
		return G.nextInt(n);
	}

	public static int Int(int min, int max) {
		return G.nextInt(max - min + 1) + min;
	}
}
