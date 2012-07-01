package algvis.linkcuttree;

public class Cut extends LinkCutAlg {
	LinkCutDSNode v;

	public Cut(LinkCutDS D, LinkCutDSNode v) {
		super(D);
		this.v = v;
		setHeader("cut");
	}
	
	public void run() {
		if (v == null) { return; }
		v.mark();
		addStep("lct-prefpath", v.getKey());
		mysuspend();
		cut(v);
		v.unmark();
	}

}
