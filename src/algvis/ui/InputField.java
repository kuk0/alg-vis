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
package algvis.ui;

import java.util.Vector;

import javax.swing.JTextField;

import algvis.core.MyRandom;
import algvis.core.Settings;
import algvis.core.WordGenerator;
import algvis.internationalization.ILabel;

/**
 * The Class InputField. This is a smart version of JTextField with methods that
 * parse the input. The input field may be asked for an integer, a vector of
 * integers, or a non-empty vector of integers (if no input is given a random
 * value is generated).
 */
public class InputField extends JTextField {
    private static final long serialVersionUID = -1263697952255226926L;
    public final static int MAX = 999;
    final ILabel sb; // status bar
    private final Settings s;

    public InputField(int cols, ILabel sb, Settings s) {
        super(cols);
        this.sb = sb;
        this.s = s;
    }

    /**
     * Returns an integer in the range 1..MAX. If no input is given, a default
     * value def is returned.
     */
    public int getInt(int def) {
        return getInt(def, 1, MAX);
    }

    /**
     * Returns an integer in the range min..max. If no input is given, a default
     * value def is returned.
     */
    public int getInt(int def, int min, int max) {
        int n = def;
        final String firstWord = this.getText().split("(\\s|,)")[0];
        try {
            n = Integer.parseInt(firstWord);
            if (n < min) {
                n = min;
                sb.setText("value too small; using the minimum value " + min
                    + " instead");
            }
            if (n > max) {
                n = max;
                sb.setText("value too high; using the maximum value " + max
                    + " instead");
            }
            sb.setText(" ");
        } catch (final NumberFormatException e) {
            sb.setText("couldn't parse an integer; using the default value "
                + def);
        }
        setText("");
        return n;
    }

    /**
     * Returns a vector of integers in the range 1..MAX. Numbers in the input
     * may be delimited by whitespaces and/or commas.
     */
    public Vector<Integer> getVI() {
        return getVI(1, MAX);
    }

    /**
     * Returns a vector of integers in the range min..max. Numbers in the input
     * may be delimited by whitespaces and/or commas.
     */
    public Vector<Integer> getVI(int min, int max) {
        boolean range = false;
        final Vector<Integer> args = new Vector<Integer>();
        final String[] tokens = this.getText().replaceAll("\\.{2,}", " .. ")
            .split("(\\s|,)+");
        for (final String t : tokens) {
            if ("..".equals(t)) {
                range = true;
            } else {
                int x = min;
                try {
                    x = Integer.parseInt(t);
                    if (x < min) {
                        x = min;
                        sb.setText("value too small; using the minimum value instead");
                    }
                    if (x > max) {
                        x = max;
                        sb.setText("value too high; using the maximum value instead");
                    }
                    if (range) {
                        final int a = args.lastElement();
                        for (int i = a + 1; i < x; ++i) {
                            args.add(i);
                        }
                        for (int i = a - 1; i > x; --i) {
                            args.add(i);
                        }
                        range = false;
                    }
                    args.add(x);
                } catch (final NumberFormatException e) {
                    sb.setText("couldn't parse an integer");
                }
            }
        }
        setText("");
        return args;
    }

    /**
     * Returns a non-empty vector of integers in the range 1..MAX. Numbers in
     * the input may be delimited by whitespaces and/or commas. If no input is
     * given, a vector with 1 random value in the range 1..MAX is returned.
     */
    public Vector<Integer> getNonEmptyVI() {
        return getNonEmptyVI(1, MAX);
    }

    /**
     * Returns a non-empty vector of integers in the range min..max. Numbers in
     * the input may be delimited by whitespaces and/or commas. If no input is
     * given, a vector with 1 random value in the range min..max is returned.
     */
    Vector<Integer> getNonEmptyVI(int min, int max) {
        final Vector<Integer> args = getVI();
        if (args.size() == 0) {
            args.add(MyRandom.Int(min, max));
            sb.setText("no input; using random value");
        }
        return args;
    }

    /**
     * Returns a vector of strings parsed from input line delimited by spaces.
     * [a-z] -> [A-Z], All chars except [A-Z] are lost.
     */
    public Vector<String> getVS() {
        final String ss = getText();
        Vector<String> result = new Vector<String>();
        if (ss.compareTo("") == 0) {
            result.add(WordGenerator.getWord(s));
        } else {
            result = WordGenerator.parseString(ss);
        }
        setText("");
        return result;
    }

    public Vector<String> getVABS() {
        return getVABS(10);
    }

    public Vector<String> getVABS(int n) {
        final String ss = getText();
        Vector<String> result = new Vector<String>();
        if (ss.compareTo("") == 0) {
            result.add(WordGenerator.getABWord(n));
        } else {
            result = WordGenerator.parseString(ss);
        }
        setText("");
        return result;
    }
}
