package algvis.core;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
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

import algvis.internationalization.LanguageListener;
import algvis.internationalization.Languages;

public class Commentary extends JEditorPane implements LanguageListener,
		HyperlinkListener {
	private static final long serialVersionUID = 9023200331860482960L;
	private VisPanel V;
	Languages L;
	JScrollPane sp;
	private int k = 0, position = 0;
	// private String text;
	private List<String> s = new ArrayList<String>(),
			pre = new ArrayList<String>(), post = new ArrayList<String>();
	private List<String[]> param = new ArrayList<String[]>();
	static SimpleAttributeSet normalAttr = new SimpleAttributeSet();
	static SimpleAttributeSet hoverAttr = new SimpleAttributeSet();
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
		clear();
	}

	public void clear() {
		position = k = 0;
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
				text.append("<B>" + str(i) + "</B");
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
			html.setInnerHTML(body, text.toString());
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void add(String u, String v, String w, String... par) {
		++position;
		pre.add(u);
		s.add(v);
		post.add(w);
		param.add(par);
		update();
	}

	public void setHeader(String h) {
		clear();
		add("<h2>", h, "</h2>");
	}

	public void addNote(String s) {
		add("<p class=\"note\">", s, "</p>");
	}

	public void addStep(String s, String... par) {
		++k;
		int scenPos = V.D.scenario.getAlgPos();
		add("<ol start=\"" + k + "\"><li class=\"step\"><p><a href=\""
				+ scenPos + "\"> ", s, "</a></p></li></ol>", par);
	}

	public void addStep(String s) {
		addStep(s, "");
	}

	public void addStep(String s, int... par) {
		String[] par2 = new String[par.length];
		for (int i = 0; i < par.length; ++i)
			par2[i] = "" + par[i];
		addStep(s, par2);
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			if (V.D.scenario.isEnabled()) {
				final int s = Integer.parseInt(e.getDescription());
				boolean ok = V.D.scenario.startNewTraverser(new Thread(
						new Runnable() {
							@Override
							public void run() {
								V.D.scenario.goToStep(s);
							}
						}));
				if (ok) {
					V.D.scenario.killTraverser();
					update();
					V.B.update();
				}
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

	public void restoreState(State state) {
		position = state.position;
		s = state.s;
		pre = state.pre;
		post = state.post;
		param = state.param;
		update();
	}

	public State getState() {
		return new State(position, s, pre, post, param);
	}

	public static class State {
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
}