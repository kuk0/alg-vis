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
package algvis.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
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

import algvis.core.StringUtils;
import algvis.internationalization.LanguageListener;
import algvis.internationalization.Languages;
import algvis.scenario.Command;

public class Commentary extends JEditorPane implements LanguageListener,
		HyperlinkListener {
	private static final long serialVersionUID = 9023200331860482960L;
	private final VisPanel V;
	private final Languages L;
	private final JScrollPane sp;
	private int k = 0, position = 0;
	private List<String> s = new ArrayList<String>(),
			pre = new ArrayList<String>(), post = new ArrayList<String>();
	private List<String[]> param = new ArrayList<String[]>();
	private boolean updatingEnabled = true;

	private static final SimpleAttributeSet normalAttr = new SimpleAttributeSet();
	private static final SimpleAttributeSet hoverAttr = new SimpleAttributeSet();
	static {
		StyleConstants.setBackground(normalAttr, Color.WHITE);
		StyleConstants.setBackground(hoverAttr, new Color(0xDB, 0xF1, 0xF9)); // #DBF1F9
	}

	public Commentary(VisPanel V, Languages L, JScrollPane sp) {
		super();
		this.V = V;
		setContentType("text/html; charset=iso-8859-2");
		setEditable(false);
		Font font = UIManager.getFont("Label.font");
		StyleSheet css = ((HTMLDocument) getDocument()).getStyleSheet();
		css.addRule("body { font-family: " + font.getFamily() + "; "
				+ "font-size: 12pt; margin: 10pt; margin-top: 0pt; " + "}");
		css.addRule(".step { margin-bottom: 5pt; }");
		css.addRule("ol { padding-left: 14px; margin: 0px; }");
		css.addRule("a { color: black; text-decoration:none; }");
		css.addRule("p.note { font-style: italic; margin: 0pt; margin-bottom: 5pt; }");
		this.L = L;
		this.sp = sp;
		L.addListener(this);
		addHyperlinkListener(this);
		setText("<html><body></body></html>");
	}

	public void clear() {
		State state = new State(position, s, pre, post, param);
		position = k = 0;
		s = new ArrayList<String>();
		pre = new ArrayList<String>();
		post = new ArrayList<String>();
		param = new ArrayList<String[]>();
		if (V.D.M.scenario.isAddingEnabled()) {
			V.D.M.scenario.add(new SetCommentaryStateCommand(state));
		}
		if (updatingEnabled) {
			setText("<html><body></body></html>");
		}
	}

	private String str(int i) {
		// if (i < 0) i = s.size() + i;
		assert (0 <= i && i < s.size());
		return pre.get(i)
				+ StringUtils.subst(L.getString(s.get(i)), param.get(i))
				+ post.get(i);
	}

	@Override
	public void languageChanged() {
		update();
	}

	public synchronized void update() {
		StringBuffer text = new StringBuffer("");
		for (int i = 0; i < s.size(); ++i) {
			if (i == position - 1) {
				text.append("<B>").append(str(i)).append("</B>");
			} else {
				text.append(str(i));
			}
		}

		HTMLDocument html = (HTMLDocument) getDocument();
		Element body = null;
		Element[] roots = html.getRootElements();
		for (int i = 0; i < roots[0].getElementCount(); i++) {
			Element element = roots[0].getElement(i);
			if (element.getAttributes().getAttribute(
					StyleConstants.NameAttribute) == HTML.Tag.BODY) {
				body = element;
				break;
			}
		}
		try {
			if (text.toString().equals("")) {
				html.setInnerHTML(body, "&nbsp;");
			} else {
				html.setInnerHTML(body, text.toString());
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		scrollDown();
	}

	public void enableUpdating(boolean updatingEnabled) {
		this.updatingEnabled = updatingEnabled;
	}

	private void scrollDown() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JScrollBar v = sp.getVerticalScrollBar();
				v.setValue(v.getMaximum() - v.getVisibleAmount());
			}
		});

	}

	void add(String u, String v, String w, String... par) {
		State state = new State(position, s, pre, post, param);
		++position;
		pre.add(u);
		s.add(v);
		post.add(w);
		param.add(par);
		if (V.D.M.scenario.isAddingEnabled()) {
			V.D.M.scenario.add(new SetCommentaryStateCommand(state));
		}
		if (updatingEnabled) {
			update();
		}
	}

	private String[] int2strArray(int[] a) {
		String[] r = new String[a.length];
		for (int i = 0; i < a.length; ++i)
			r[i] = "" + a[i];
		return r;
	}

	public void setHeader(String h) {
		clear();
		add("<h2>", h, "</h2>");
	}

	public void setHeader(String h, String... par) {
		clear();
		add("<h2>", h, "</h2>", par);
	}

	public void setHeader(String h, int... par) {
		clear();
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
		++k;
		int scenPos = V.D.M.scenario.getAlgPos();
		add("<ol start=\"" + k + "\"><li class=\"step\"><p><a href=\""
				+ scenPos + "\"> ", s, "</a></p></li></ol>", par);
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
			if (V.D.M.scenario.isEnabled()) {
				V.D.M.scenario.goBeforeStep(Integer.parseInt(e.getDescription()),
						false);
				V.D.M.scenario.next(true, true);
			}
		} else {
			Element element = e.getSourceElement();
			int start = element.getStartOffset();
			int length = element.getEndOffset() - start;
			HTMLDocument html = ((HTMLDocument) getDocument());

			if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
				html.setParagraphAttributes(start, length, hoverAttr, false);
			}
			if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
				html.setParagraphAttributes(start, length, normalAttr, false);
			}
		}
	}

	void restoreState(State state) {
		position = state.position;
		s = state.s;
		pre = state.pre;
		post = state.post;
		param = state.param;
		if (updatingEnabled) {
			update();
		}
	}

	State getState() {
		return new State(position, s, pre, post, param);
	}

	private class State {
		private final int position;
		private final List<String> s, pre, post;
		private final List<String[]> param;

		public State(int position, List<String> s, List<String> pre,
				List<String> post, List<String[]> param) {
			this.position = position;
			this.s = s;
			this.pre = pre;
			this.post = post;
			this.param = param;
		}
	}

	private class SetCommentaryStateCommand implements Command {
		private final State fromState, toState;

		public SetCommentaryStateCommand(State fromState) {
			this.fromState = fromState;
			this.toState = getState();
		}

		@Override
		public org.jdom.Element getXML() {
			org.jdom.Element e = new org.jdom.Element("saveCommentary");
			return e;
		}

		@Override
		public void execute() {
			restoreState(toState);
		}

		@Override
		public void unexecute() {
			restoreState(fromState);
		}
	}
}
