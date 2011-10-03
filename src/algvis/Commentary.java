package algvis;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.JEditorPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class Commentary extends JEditorPane {
	private static final long serialVersionUID = 9023200331860482960L;
	AlgVis a;
	JScrollPane sp;
	int k = 0;
	String text;
	List<String> s = new ArrayList<String> (),
	           pre = new ArrayList<String> (),
	          post = new ArrayList<String> ();
	ArrayList<String[]> param = new ArrayList<String[]> ();

	public Commentary(AlgVis a, JScrollPane sp) {
		super();
		setContentType("text/html; charset=iso-8859-2");
		setEditable(false);
		this.a = a;
		this.sp = sp;
	}
	
	public void clear() {
		text = "";
		k = 0;
		s.clear();
		pre.clear();
		post.clear();
		param.clear();
	}
	
	private String str(int i) {
		//if (i < 0) i = s.size() + i;
		assert (0 <= i && i < s.size()); 
		return pre.get(i) + StringUtils.subst(a.getString(s.get(i)), param.get(i)) + post.get(i);
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
    
	
	public void refresh() {
		text = "";
		for (int i=0; i<s.size(); ++i) text += str(i) + str(i) + str(i) + str(i) + str(i); 			
		super.setText(text);
		scrollDown();
	}

	public void add(String u, String v, String w, String ...par) {
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
}