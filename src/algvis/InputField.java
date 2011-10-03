package algvis;

import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTextField;

public class InputField extends JTextField {
	private static final long serialVersionUID = -1263697952255226926L;
	final static int MAX = 999;
	Random G;
	ILabel sb; // status bar

	public InputField(int cols, ILabel sb) {
		super(cols);
		G = new Random(System.currentTimeMillis());
		this.sb = sb;
	}

	public int getInt(int def) {
		return getInt(def, 1, MAX);
	}

	public int getInt(int def, int min, int max) {
		int n = def;
		StringTokenizer st = new StringTokenizer(this.getText());
		try {
			n = Integer.parseInt(st.nextToken());
		} catch (Exception e) {
			sb.setText("couldn't parse an integer; using the default value " + def);
		} finally {
			if (n < min) {
				n = min;
				sb.setText("value too small; using the minimum value " + min + " instead");
			}
			if (n > max) {
				n = max;
				sb.setText("value too high; using the maximum value " + max + " instead");
			}
		}
		return n;
	}

	public Vector<Integer> getVI() {
		return getVI(1, MAX);
	}

	public Vector<Integer> getVI(int min, int max) {
		Vector<Integer> args = new Vector<Integer>();
		StringTokenizer st = new StringTokenizer(this.getText());
		while (st.hasMoreTokens()) {
			int x = min;
			try {
				x = Integer.parseInt(st.nextToken());
			} catch (Exception e) {
				sb.setText("couldn't parse an integer");
			} finally {
				if (x < min) {
					x = min;
					sb.setText("value too small; using the minimum value instead");
				}
				if (x > max) {
					x = max;
					sb.setText("value too high; using the maximum value instead");
				}
				args.add(x);
			}
		}
		return args;
	}

	public Vector<Integer> getNonEmptyVI() {
		return getNonEmptyVI(1, MAX);
	}

	public Vector<Integer> getNonEmptyVI(int min, int max) {
		Vector<Integer> args = getVI();
		if (args.size() == 0) {
			args.add(G.nextInt(max - min + 1) + min);
			sb.setText("no input; using random value");
		}
		return args;
	}
}
