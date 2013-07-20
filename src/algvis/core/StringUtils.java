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

/**
 * The Class StringUtils. Contains basic string methods.
 */
public class StringUtils {
	// substitute values param for parameters in the string s
	// parameters are denoted #1, #2, #3, ...
	public static String subst(String s, int... param) {
		for (int i = 0; i < param.length; ++i) {
			s = s.replaceAll("#" + Integer.toString(i + 1),
					Integer.toString(param[i]));
		}
		return s;
	}

	public static String subst(String s, String... param) {
		for (int i = 0; i < param.length; ++i) {
			s = s.replaceAll("#" + Integer.toString(i + 1), param[i]);
		}
		return s.replaceAll("##", "#");
	}

	// TODO: only until we get rid of the commentary
	public static String unHtml(String s) {
		return s.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&le;", "\u2264").replaceAll("&ge;", "\u2265")
				.replaceAll("&lang;", "<").replaceAll("&rang;", ">");
	}

	private static final String ZEROES = "000000000000";
	private static final String BLANKS = "            ";

	public static String format(double val, int n, int w) { // rounding
		double incr = 0.5;
		for (int j = n; j > 0; j--) {
			incr /= 10;
		}
		val += incr;
		String s = Double.toString(val);
		final int n1 = s.indexOf('.');
		final int n2 = s.length() - n1 - 1;
		if (n > n2) {
			s = s + ZEROES.substring(0, n - n2);
		} else if (n2 > n) {
			s = s.substring(0, n1 + n + 1);
		}
		if (w > 0 && w > s.length()) {
			s = BLANKS.substring(0, w - s.length()) + s;
		} else if (w < 0 && (-w) > s.length()) {
			w = -w;
			s = s + BLANKS.substring(0, w - s.length());
		}
		return s;
	}
}
