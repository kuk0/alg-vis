package algvis.ds.intervaltree.fenwick;

public class FenwickIdx {

	/**
	 * Get idx as a binary string as long as idxmax would be
	 */
	private static String getBinary(int idx, int idxmax) {
		// Get the original length to make it uniform
		int length = Integer.toBinaryString(idxmax).length();
		String zeros = new String(new char[length]).replace('\0', '0');

		String sidx = (zeros + Integer.toBinaryString(idx));
		return sidx.substring(sidx.length() - length);
	}

	/**
	 * Return position (from LSB, counting from 0) of the last set bit
	 */
	private static int lastBit(int idx) {
		int i = 0;
		for (int d = idx & -idx; d > 0; i++, d /= 2);
		return i;
	}

	/**
	 * Highlight the i-th digit by surrounding it with 'u' html tags
	 */
	private static String highlightDigit(String s, int i) {
		return s.substring(0, s.length() - i)
				+ "<u>"
				+ s.substring(s.length() - i, s.length() - i + 1)
				+ "</u>"
				+ s.substring(s.length() - i + 1);
	}

	/**
	 * Returns decimal idx/newidx and binary idx/newidx for prefix sum
	 * calculatio with highlighted changed bit, formatted to be as long as
	 * idxmax
	 * 
	 * @return Array of {idx, idx decimal, newidx, newidx decimal}
	 */
	public static String[] formatNextSumIdx(int idx, int idxmax) {
		// Old idx binary string
		String sidx = getBinary(idx, idxmax);

		// New idx binary string
		int newidx = idx - (idx & -idx);
		String snewidx = getBinary(newidx, idxmax);

		// Digit to highlight
		int i = lastBit(idx);

		// Highlight the changed digit
		sidx = highlightDigit(sidx, i);
		snewidx = highlightDigit(snewidx, i);

		return new String[] { sidx, idx + "", snewidx, newidx + "" };
	}

	/**
	 * Like formatNextSumIdx but for insert operation
	 * 
	 * @return Array of {idx, idx decimal, last bit, last bit decimal, newidx,
	 *         newidx decimal}
	 */
	public static String[] formatNextInsertIdx(int idx, int idxmax) {
		// Old idx binary string
		String sidx = getBinary(idx, idxmax);

		// New idx binary string
		int newidx = idx + (idx & -idx);
		if (newidx > idxmax) {
			// Pretend to overflow when going above root
			newidx = 0;
		}
		String snewidx = getBinary(newidx, idxmax);

		// Last set bit
		int i = lastBit(idx);
		int last = 1 << (i - 1);
		String slast = getBinary(last, idxmax);

		// Highlight
		sidx = highlightDigit(sidx, i);
		slast = highlightDigit(slast, i);
		
		return new String[] { sidx, idx + "", slast, last + "", snewidx,
				newidx + "" };

	}

}
