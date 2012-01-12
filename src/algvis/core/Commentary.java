package algvis.core;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.JEditorPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import algvis.internationalization.LanguageListener;
import algvis.internationalization.Languages;

public class Commentary extends JEditorPane implements LanguageListener {
	private static final long serialVersionUID = 9023200331860482960L;
	Languages L;
	JScrollPane sp;
	private int k = 0, position = 0;
	private String text;
	private List<String> s = new ArrayList<String> (),
	           pre = new ArrayList<String> (),
	          post = new ArrayList<String> ();
	private List<String[]> param = new ArrayList<String[]> ();

	public Commentary(Languages L, JScrollPane sp) {
		super();
		setContentType("text/html; charset=iso-8859-2");
		setEditable(false);
		this.L = L;
		this.sp = sp;
		L.addListener(this);
	}

	private void clear() {
		text = "";
		position = k = 0;
		s = new ArrayList<String>();
		pre = new ArrayList<String>();
		post = new ArrayList<String>();
		param = new ArrayList<String[]>();
	}
	
	private String str(int i) {
		//if (i < 0) i = s.size() + i;
		assert (0 <= i && i < s.size()); 
		return pre.get(i) + StringUtils.subst(L.getString(s.get(i)), param.get(i)) + post.get(i);
	}
	
	private void scrollDown() {
		JScrollBar bar = sp.getVerticalScrollBar();
		if (bar == null) return;
		BoundedRangeModel model = bar.getModel();
	    if ((model.getExtent() + model.getValue()) == model.getMaximum()) return;
	    
	    EventQueue.invokeLater(new Runnable() {
            public void run() {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                    	Rectangle r = getVisibleRect();
                    	r.y = getHeight() - r.height;
                    	scrollRectToVisible(r);
                    }
                });
            }
        });
	}
    
	
	public void languageChanged() {
		text = "";
		for (int i=0; i<position; ++i) text += str(i); //+ str(i) + str(i) + str(i) + str(i); 			
		super.setText(text);
		scrollDown();
	}

	private void add(String u, String v, String w, String ...par) {
		++position;
		pre.add(u);
		s.add(v);
		post.add(w);
		param.add(par);
		text += str(s.size()-1);
		super.setText(text);
		scrollDown();
	}
	
	public void setHeader(String h) {
		clear();
		add("<h3>", h, "</h3>");
	}

	public void setText(String s) {
		++k;
		add (""+k+". ", s, "<br>");
	}
	
	public void setText(String s, String... par) {
		++k;
		add (""+k+". ", s, "<br>", par);
	}

	public void setText(String s, int... par) {
		++k;
		String[] par2 = new String[par.length];
		for (int i = 0; i<par.length; ++i) par2[i] = "" + par[i];
		add (""+k+". ", s, "<br>", par2);
	}

	public void restoreState(State state) {
		k = state.k;
		position = state.position;
		s = state.s;
		pre = state.pre;
		post = state.post;
		param = state.param;
		languageChanged();
	}

	public State getState() {
		return new State(k, position, s, pre, post, param);
	}

	public static class State {
		private final int k, position;
		private final List<String> s, pre, post;
		private final List<String[]> param;

		public State(int k, int position, List<String> s, List<String> pre,
				List<String> post, List<String[]> param) {
			this.k = k;
			this.position = position;
			this.s = s;
			this.pre = pre;
			this.post = post;
			this.param = param;
		}
	}
}