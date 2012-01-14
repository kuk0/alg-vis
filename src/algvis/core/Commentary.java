package algvis.core;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
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
	Languages L;
	JScrollPane sp;
	int k = 0;
	String text;
	List<String> s = new ArrayList<String>(), pre = new ArrayList<String>(),
			post = new ArrayList<String>();
	ArrayList<String[]> param = new ArrayList<String[]>();
	static SimpleAttributeSet normalAttr = new SimpleAttributeSet();
	static SimpleAttributeSet hoverAttr = new SimpleAttributeSet();
	static {
		StyleConstants.setBackground(normalAttr, Color.WHITE);
		StyleConstants.setBackground(hoverAttr, new Color(0xDB, 0xF1, 0xF9)); // #DBF1F9
	}

	public Commentary(Languages L, JScrollPane sp) {
		super();
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
		text = "";
		k = 0;
		s.clear();
		pre.clear();
		post.clear();
		param.clear();
		setText("<html><body></body></html>");
	}

	private String str(int i) {
		// if (i < 0) i = s.size() + i;
		assert (0 <= i && i < s.size());
		return pre.get(i)
				+ StringUtils.subst(L.getString(s.get(i)), param.get(i))
				+ post.get(i);
	}

	private void scrollDown() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				final JScrollBar v = sp.getVerticalScrollBar();
				v.setValue(v.getMaximum() - v.getVisibleAmount());
			}
		});
	}

	public void languageChanged() {
		StringBuffer text = new StringBuffer("");
		for (int i = 0; i < s.size(); ++i)
			text.append(str(i));
		setText(text.toString());
		scrollDown();
	}

	public void add(String u, String v, String w, String... par) {
		pre.add(u);
		s.add(v);
		post.add(w);
		param.add(par);
		HTMLDocument html = (HTMLDocument) getDocument();
		Element body = null;
		Element[] roots = html.getRootElements();
		for( int i = 0; i < roots[0].getElementCount(); i++ ) {
		    Element element = roots[0].getElement( i );
		    if(element.getAttributes().getAttribute( StyleConstants.NameAttribute ) == HTML.Tag.BODY ) {
		        body = element;
		        break;
		    }
		}
		try {
			html.insertBeforeEnd(body, str(s.size() - 1));
		} catch (Exception e) {
		}
		//text += str(s.size() - 1);
		//super.setText(text);
		scrollDown();
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
		add("<ol start=\"" + k + "\"><li class=\"step\"><p><a href=\"" + k
				+ "\"> ", s, "</a></p></li></ol>", par);
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

	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			System.out.println(e.getDescription());
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
}