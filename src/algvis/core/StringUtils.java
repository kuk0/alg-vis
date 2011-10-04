package algvis.core;

/**
 * The Class StringUtils.
 * Contains basic string methods.
 */
public class StringUtils {
	// substitute values param for parameters in the string s
	// parameters are denoted #1, #2, #3, ...
	public static String subst(String s, int... param) {
		for (int i = 0; i < param.length; ++i) {
			s = s.replaceAll("#" + Integer.toString(i + 1), Integer
					.toString(param[i]));
		}
		return s;
	}
	
	public static String subst(String s, String... param) {
		for (int i = 0; i < param.length; ++i) {
			s = s.replaceAll("#" + Integer.toString(i + 1), param[i]);
		}
		return s;
	}

	static final String ZEROES = "000000000000";
	static final String BLANKS = "            ";
	public static String format(double val, int n, int w) { // rounding
		double incr = 0.5;
		for (int j = n; j > 0; j--) {
			incr /= 10;
		}
		val += incr;
		String s = Double.toString(val);
		int n1 = s.indexOf('.');
		int n2 = s.length() - n1 - 1;
		if (n > n2) {
			s = s + ZEROES.substring(0, n - n2);
		} else if (n2 > n) {
			s = s.substring(0, n1 + n + 1);
		}
		if (w > 0 & w > s.length()) {
			s = BLANKS.substring(0, w - s.length()) + s;
		} else if (w < 0 & (-w) > s.length()) {
			w = -w;
			s = s + BLANKS.substring(0, w - s.length());
		}
		return s;
	}
}
