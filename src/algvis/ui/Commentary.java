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

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.StateEditable;

import algvis.core.StringUtils;
import algvis.core.history.HashtableStoreSupport;
import algvis.internationalization.LanguageListener;
import algvis.internationalization.Languages;

public class Commentary extends JEditorPane implements LanguageListener,
		HyperlinkListener, StateEditable {
	private static final long serialVersionUID = 9023200331860482960L;
	private final VisPanel panel;
	private final JScrollPane sp;
	private int indexOfNextStep = 0, currentPosition = -1;
	private List<String> s = new ArrayList<String>(),
			pre = new ArrayList<String>(), post = new ArrayList<String>();
	private List<String[]> param = new ArrayList<String[]>();

	private static final SimpleAttributeSet normalAttr = new SimpleAttributeSet();
	private static final SimpleAttributeSet hoverAttr = new SimpleAttributeSet();
	static {
		StyleConstants.setBackground(normalAttr, Color.WHITE);
		StyleConstants.setBackground(hoverAttr, new Color(0xDB, 0xF1, 0xF9)); // #DBF1F9
	}
	private final String hash = Integer.toString(hashCode());

	public Commentary(VisPanel panel, JScrollPane sp) {
		super();
		this.panel = panel;
		setContentType("text/html; charset=iso-8859-2");
		setEditable(false);
		final Font font = UIManager.getFont("Label.font");
		final StyleSheet css = ((HTMLDocument) getDocument()).getStyleSheet();
		css.addRule("body { font-family: " + font.getFamily() + "; "
				+ "font-size: 12pt; margin: 10pt; margin-top: 0pt; " + "}");
		css.addRule(".step { margin-bottom: 5pt; }");
		css.addRule("ol { padding-left: 14px; margin: 0px; }");
		css.addRule("a { color: black; text-decoration:none; }");
		css.addRule("p.note { font-style: italic; margin: 0pt; margin-bottom: 5pt; }");
		this.sp = sp;
		Languages.addListener(this);
		addHyperlinkListener(this);
		setText("<html><body></body></html>");
	}

	public synchronized void clear() {
		currentPosition = -1;
		indexOfNextStep = 0;
		s = new ArrayList<String>();
		pre = new ArrayList<String>();
		post = new ArrayList<String>();
		param = new ArrayList<String[]>();
		setText("<html><body></body></html>");
	}

	private String str(int i) {
		// if (i < 0) i = s.size() + i;
		assert (0 <= i && i < s.size());
		return pre.get(i)
				+ StringUtils
						.subst(Languages.getString(s.get(i)), param.get(i))
				+ post.get(i);
	}

	@Override
	public void languageChanged() {
		refresh();
	}

	public void refresh() {
		final StringBuilder text = new StringBuilder("");
		synchronized (this) {
			for (int i = 0; i < s.size(); ++i) {
				if (i == currentPosition) {
					text.append("<B>").append(str(i)).append("</B>");
				} else {
					text.append(str(i));
				}
			}
		}
		final HTMLDocument html = (HTMLDocument) getDocument();
		Element body = null;
		final Element[] roots = html.getRootElements();
		for (int i = 0; i < roots[0].getElementCount(); i++) {
			final Element element = roots[0].getElement(i);
			if (element.getAttributes().getAttribute(
					StyleConstants.NameAttribute) == HTML.Tag.BODY) {
				body = element;
				break;
			}
		}
		try {
			synchronized (this) {
				if (text.toString().equals("")) {
					html.setInnerHTML(body, "&nbsp;");
				} else {
					html.setInnerHTML(body, text.toString());
				}
			}
		} catch (final BadLocationException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		scrollDown();
	}

	private void scrollDown() {
		final JScrollBar v = sp.getVerticalScrollBar();
		v.setValue(v.getMaximum() - v.getVisibleAmount());
	}

	private synchronized void add(String u, String v, String w, String... par) {
		++currentPosition;
		pre.add(u);
		s.add(v);
		post.add(w);
		param.add(par);
	}

	private String[] int2strArray(int[] a) {
		final String[] r = new String[a.length];
		for (int i = 0; i < a.length; ++i) {
			r[i] = "" + a[i];
		}
		return r;
	}

	public void setHeader(String h) {
		// clear();
		add("<h2>", h, "</h2>");
	}

	public void setHeader(String h, String... par) {
		// clear();
		add("<h2>", h, "</h2>", par);
	}

	public void setHeader(String h, int... par) {
		setHeader(h, int2strArray(par));
	}

	public void addNote(String s) {
		add("<p class=\"note\">", s, "</p>");
	}

	public void addNote(String s, String... par) {
		add("<p class=\"note\">", s, "</p>", par);
	}

	public void addNote(String s, int... par) {
		addNote(s, int2strArray(par));
	}

	public void addStep(String s, String... par) {
		add("<ol start=\"" + (indexOfNextStep + 1)
				+ "\"><li class=\"step\"><p><a href=\""
				+ panel.history.getLastEditId() + "\"> ", s,
				"</a></p></li></ol>", par);
		++indexOfNextStep;
	}

	public void addStep(String s) {
		addStep(s, "");
	}

	public void addStep(String s, int... par) {
		addStep(s, int2strArray(par));
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			panel.history.goTo(Integer.parseInt(e.getDescription()));
			panel.refresh();
		} else {
			final Element element = e.getSourceElement();
			final int start = element.getStartOffset();
			final int length = element.getEndOffset() - start;
			final HTMLDocument html = ((HTMLDocument) getDocument());

			if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
				html.setParagraphAttributes(start, length, hoverAttr, false);
			}
			if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
				html.setParagraphAttributes(start, length, normalAttr, false);
			}
		}
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		HashtableStoreSupport.store(state, hash + "currentPosition",
				currentPosition);
		HashtableStoreSupport.store(state, hash + "s", s);
		HashtableStoreSupport.store(state, hash + "pre", pre);
		HashtableStoreSupport.store(state, hash + "post", post);
		HashtableStoreSupport.store(state, hash + "param", param);
	}

	@Override
	public synchronized void restoreState(Hashtable<?, ?> state) {
		final Object position = state.get(hash + "currentPosition");
		if (position != null) {
			this.currentPosition = (Integer) HashtableStoreSupport
					.restore(position);
		}
		final Object s = state.get(hash + "s");
		if (s != null) {
			this.s = (List<String>) HashtableStoreSupport.restore(s);
		}
		final Object pre = state.get(hash + "pre");
		if (pre != null) {
			this.pre = (List<String>) HashtableStoreSupport.restore(pre);
		}
		final Object post = state.get(hash + "post");
		if (post != null) {
			this.post = (List<String>) HashtableStoreSupport.restore(post);
		}
		final Object param = state.get(hash + "param");
		if (param != null) {
			this.param = (List<String[]>) HashtableStoreSupport.restore(param);
		}
	}
}
